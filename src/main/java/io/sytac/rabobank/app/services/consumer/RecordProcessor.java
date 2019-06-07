package io.sytac.rabobank.app.services.consumer;

import io.sytac.rabobank.app.model.Irregularities;
import io.sytac.rabobank.app.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

@Service
@Slf4j
public class RecordProcessor {

    private BlockingQueue<Transaction> blockingQueue;
    private Map<Integer, Transaction> concurrentMap;

    @Autowired
    public RecordProcessor(BlockingQueue<Transaction> blockingQueue, Map<Integer, Transaction> concurrentMap) {
        this.blockingQueue = blockingQueue;
        this.concurrentMap = concurrentMap;
    }

    /**
     * It will validate the records. Then if the @concurrentMap has already a key == transactionReference,
     * it will set the transaction has duplicated adding an irregularity to a the list of Irregularites
     * @throws InterruptedException
     */
    public void processRecord() throws InterruptedException{
            Transaction transaction = blockingQueue.take();
            validateRecords(transaction);
            if(concurrentMap.containsKey(transaction.getTransactionReference())){
                transaction.addIrregularity(Irregularities.DUPLICATED_REF_NUMB);
                concurrentMap.get(transaction.getTransactionReference()).addDuplicatedTransaction(transaction);
            }else {
                concurrentMap.put(transaction.getTransactionReference(), transaction);
            }
    }

    private void validateRecords(Transaction transaction){
        BigDecimal startBalance = new BigDecimal(transaction.getStartBalance()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal endBalance = new BigDecimal(transaction.getEndBalance()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal mutation = new BigDecimal(transaction.getMutation()).setScale(2, RoundingMode.HALF_UP);
        if(startBalance.add(mutation).compareTo(endBalance) != 0){
            transaction.setNotValid(true);
            transaction.addIrregularity(Irregularities.BALANCE_NOT_VALID);
        }
    }
}
