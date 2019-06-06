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
public class ConsumerJob{
    @Autowired
    private ConsumerExitSyncronizer consumerExitSyncronizer;
    @Autowired
    private BlockingQueue<Transaction> blockingQueue;

    @Autowired
    private Map<Integer, Transaction> concurrentMap;


    @Async
    public void processRecord() {
        while (!consumerExitSyncronizer.getNotifyConsumersShutdown()){
            log.info("#### running" + Thread.currentThread().getName());
            try {
                Transaction transaction = blockingQueue.take();
                validateRecords(transaction);
                if(concurrentMap.containsKey(transaction.getTransactionReference())){
                    transaction.addIrregularity(Irregularities.DUPLICATED_REF_NUMB);
                    concurrentMap.get(transaction.getTransactionReference()).addDuplicatedTransaction(transaction);
                }else {
                    concurrentMap.put(transaction.getTransactionReference(), transaction);
                }
            } catch (InterruptedException ex){
                if(!consumerExitSyncronizer.getNotifyConsumersShutdown()){
                    ex.printStackTrace();
                }
            }

        }

        log.info("@@@@@ Terminated: " + Thread.currentThread().getName());

    }

    public static void validateRecords(Transaction transaction){
        BigDecimal startBalance = new BigDecimal(transaction.getStartBalance()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal endBalance = new BigDecimal(transaction.getEndBalance()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal mutation = new BigDecimal(transaction.getMutation()).setScale(2, RoundingMode.HALF_UP);
        if(startBalance.add(mutation).compareTo(endBalance) != 0){
            transaction.setNotValid(true);
            transaction.addIrregularity(Irregularities.BALANCE_NOT_VALID);
        }
    }
}
