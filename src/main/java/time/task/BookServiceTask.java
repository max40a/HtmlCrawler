package time.task;

import db.book.BookHandleService;
import org.quartz.*;

public class BookServiceTask implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        BookHandleService service = (BookHandleService) dataMap.get("service");
        service.executeService();
    }
}