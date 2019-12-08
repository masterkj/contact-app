package com.internetapplication.ws.jobs.exportcontacts.chunks;

import com.internetapplication.ws.jobs.exportcontacts.ContactFetcher;
import com.internetapplication.ws.jobs.exportcontacts.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;

public class LinesReader implements ItemReader<Line>, StepExecutionListener {

    private final Logger logger = LoggerFactory.getLogger(LinesReader.class);
    private ContactFetcher cf;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        cf = ContactFetcher.getInstance();

        logger.debug("Contact Fetcher initialized.");
    }

    @Override
    public Line read() throws Exception {
        //        if (line != null) logger.debug("Read line: " + line.toString());
        return cf.fetch();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
//        fu.closeReader();
        logger.debug("Line Reader ended.");
        return ExitStatus.COMPLETED;
    }
}