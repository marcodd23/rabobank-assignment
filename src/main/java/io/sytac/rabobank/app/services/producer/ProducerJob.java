package io.sytac.rabobank.app.services.producer;

import io.sytac.rabobank.app.ConsumerExitSyncronizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProducerJob {

    @Autowired
    private CsvParser csvParser;
    @Autowired
    private ConsumerExitSyncronizer consumerExitSyncronizer;

    public void produce(String filename) throws InterruptedException {
        csvParser.parseDocument(filename);
        log.info("##### Running" + Thread.currentThread().getName());
        //Thread.sleep(1000);
        //log.info("@@@@@ Stopping producers thread " + Thread.currentThread().getName());
        consumerExitSyncronizer.setNotifyConsumersShutdown(true);
    }
}
