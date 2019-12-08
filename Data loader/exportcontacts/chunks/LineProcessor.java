package com.internetapplication.ws.jobs.exportcontacts.chunks;


import com.internetapplication.ws.jobs.exportcontacts.Line;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;

public class LineProcessor implements ItemProcessor<Line, Line>, StepExecutionListener {


    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public Line process(Line line) throws Exception {
        return line;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}