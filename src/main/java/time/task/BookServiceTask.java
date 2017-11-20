package time.task;

import db.book.BookService;
import org.quartz.*;

public class BookServiceTask implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        BookService service = (BookService) dataMap.get("bookService");
        service.executeService();
    }
}