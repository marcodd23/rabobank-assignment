package io.sytac.rabobank.app.services.consumer;

import io.sytac.rabobank.app.ConsumerExitSyncronizer;
import io.sytac.rabobank.app.model.Transaction;
import io.sytac.rabobank.app.model.Irregularities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

@Service
@Slf4j
public class ConsumerJob {

    @Autowired
    private ConsumerExitSyncronizer consumerExitSyncronizer;
    @Autowired
    private RecordProcessor recordProcessor;
    @Autowired
    private BlockingQueue<Transaction> blockingQueue;

    @Async
    public void runConsumer() {
        while (!consumerExitSyncronizer.getNotifyConsumersShutdown()) {
            log.info("#### running" + Thread.currentThread().getName());
            processRecord();
        }
        while (!blockingQueue.isEmpty()) {
            processRecord();
        }

        log.info("@@@@@ Terminated: " + Thread.currentThread().getName());

    }

    private void processRecord() {
        try {
            recordProcessor.processRecord();
        } catch (InterruptedException ex) {
            if (!consumerExitSyncronizer.getNotifyConsumersShutdown()) {
                ex.printStackTrace();
            }
        }
    }
}
