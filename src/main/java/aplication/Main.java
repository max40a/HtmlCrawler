package aplication;

import book.providers.BookProvider;
import book.providers.LocalBookProvider;
import book.providers.RemoteBookProvider;
import com.google.gson.Gson;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import convertors.AbstractBookParser;
import convertors.BookParser;
import db.book.BookService;
import db.book.BookKeeper;
import db.url.UrlSieve;
import db.url.UrlsGenerator;
import db.url.UrlsSupplier;
import http.client.HttpsClient;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import time.task.BookServiceTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    private static final String PATH_TO_LOCAL_LINKS = "exempls/links.txt";
    private static final String PATH_TO_LOG_PROPERTY_CONFIG_FILE = "log/log4j.properties";
    private static final String PATH_TO_URL_PROVIDER_DB_PROPERTIES = "db/url.provider.properties";

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure(Main.class.getClassLoader().getResource(PATH_TO_LOG_PROPERTY_CONFIG_FILE));
        Properties dbProperties = loadProperties(PATH_TO_URL_PROVIDER_DB_PROPERTIES);
        MysqlConnectionPoolDataSource dataSource = initDataSource(dbProperties);

        AbstractBookParser parser = new BookParser();

        //*LOCAL PART*/
        BookProvider localProvider = new LocalBookProvider();
        List<String> links = IOUtils.readLines(
                Main.class.getClassLoader().getResourceAsStream(PATH_TO_LOCAL_LINKS),
                StandardCharsets.UTF_8.name());

        for (String link : links) {
            try {
                localProvider.getBookHtml(new URL(link)).ifPresent(s -> System.out.println(parser.convertHtmlToBook(s)));
            } catch (Exception e) {
                log.error(e);
            }
        }

       /* REMOTE PART*/
        HttpsClient client = new HttpsClient();
        UrlSieve sieve = new UrlSieve(dataSource);
        UrlsGenerator urlsGenerator = new UrlsGenerator(dataSource);
        Gson bookToJsonConverter = new Gson();
        BookKeeper bookKeeper = new BookKeeper(dataSource);
        UrlsSupplier urlsSupplier = new UrlsSupplier(dataSource);

        BookProvider bookProvider = new RemoteBookProvider(client, sieve);
        BookService service = new BookService(urlsSupplier, bookProvider, parser, bookToJsonConverter, bookKeeper);

        int from = 709_010;
        int to = 709_030;
        urlsGenerator.generateUrls(from, to);

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("service", service);

        JobDetail jobDetail = JobBuilder.newJob(BookServiceTask.class)
                .withIdentity("parserjob", "group1")
                .usingJobData(jobDataMap)
                .build();

        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("parsertrigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInMinutes(1)
                        .withRepeatCount(4))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
        TimeUnit.MINUTES.sleep(5);
        scheduler.shutdown(true);
    }

    private static MysqlConnectionPoolDataSource initDataSource(Properties dbProperties) throws IOException {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName(dbProperties.getProperty("jdbc.url"));
        dataSource.setDatabaseName(dbProperties.getProperty("jdbc.dbname"));
        dataSource.setUser(dbProperties.getProperty("jdbc.username"));
        dataSource.setPassword(dbProperties.getProperty("jdbc.password"));
        dataSource.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return dataSource;
    }

    private static Properties loadProperties(String path) throws IOException {
        Properties properties = new Properties();
        try (InputStream in = Main.class.getClassLoader().getResourceAsStream(path)) {
            properties.load(in);
        }
        return properties;
    }
}