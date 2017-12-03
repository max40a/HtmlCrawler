package aplication;

import book.providers.BookProvider;
import book.providers.LocalBookProvider;
import book.providers.RemoteBookProvider;
import com.google.gson.Gson;
import convertors.AbstractBookParser;
import convertors.BookParser;
import db.DataBaseUtil;
import db.book.BookDao;
import db.book.BookService;
import db.url.UrlSieve;
import db.url.UrlsSupplier;
import http.client.HttpsClient;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import time.task.BookServiceTask;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    private static final String PATH_TO_LOCAL_LINKS = "exempls/links.txt";
    private static final String PATH_TO_LOG_PROPERTY_CONFIG_FILE = "log/log4j.properties";
    private static final String PATH_TO_URL_PROVIDER_DB_PROPERTIES = "db/url.provider.properties";

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure(Main.class.getClassLoader().getResource(PATH_TO_LOG_PROPERTY_CONFIG_FILE));
        DataSource dataSource = DataBaseUtil.getDataSource(PATH_TO_URL_PROVIDER_DB_PROPERTIES);
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
        Gson bookToJsonConverter = new Gson();
        BookDao bookDao = new BookDao(dataSource);
        UrlsSupplier urlsSupplier = new UrlsSupplier(dataSource);
        BookProvider bookProvider = new RemoteBookProvider(client, sieve);

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        BookService bookService = new BookService(urlsSupplier, bookProvider, parser, bookToJsonConverter, bookDao, validator);

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("bookService", bookService);

        JobDetail jobDetail = JobBuilder.newJob(BookServiceTask.class)
                .withIdentity("parserJob", "group1")
                .usingJobData(jobDataMap)
                .build();

        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("parserTrigger", "group1")
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
}