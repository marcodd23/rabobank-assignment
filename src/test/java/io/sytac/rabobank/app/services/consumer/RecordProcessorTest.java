package io.sytac.rabobank.app.services.consumer;

import io.sytac.rabobank.app.ConsumerExitSyncronizer;
import io.sytac.rabobank.app.model.Irregularities;
import io.sytac.rabobank.app.model.Transaction;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RecordProcessorTest {

    @Test
    public void testTransactionValid() throws InterruptedException {
        Transaction t1 = Transaction.builder()
                .transactionReference(156108)
                .iban("NL69ABNA0433647324")
                .description("Flowers from Erik de Vries")
                .startBalance(Double.parseDouble("13.92"))
                .mutation(Double.parseDouble("-7.25"))
                .endBalance(Double.parseDouble("6.67"))
                .isNotValid(false)
                .duplicatedTransactions(new ArrayList<>())
                .irregularities(new ArrayList<>())
                .build();
        BlockingQueue<Transaction> blockingQueue = new LinkedBlockingDeque<>(100);
        Map<Integer, Transaction> concurrentMap = new ConcurrentHashMap<>();
        RecordProcessor recordProcessor = new RecordProcessor(blockingQueue, concurrentMap);
        blockingQueue.add(t1);
        recordProcessor.processRecord();
        Assertions.assertThat(concurrentMap.containsKey(156108));
        Assertions.assertThat(concurrentMap.get(156108).isNotValid()).isFalse();
        System.out.println("sdfgdfg");
    }

    @Test
    public void testTransactionBalanceNotValid() throws InterruptedException {
        Transaction t1 = Transaction.builder()
                .transactionReference(156108)
                .iban("NL69ABNA0433647324")
                .description("Flowers from Erik de Vries")
                .startBalance(Double.parseDouble("13.92"))
                .mutation(Double.parseDouble("-10.25"))
                .endBalance(Double.parseDouble("6.67"))
                .isNotValid(false)
                .duplicatedTransactions(new ArrayList<>())
                .irregularities(new ArrayList<>())
                .build();

        BlockingQueue<Transaction> blockingQueue = new LinkedBlockingDeque<>(100);
        Map<Integer, Transaction> concurrentMap = new ConcurrentHashMap<>();
        RecordProcessor recordProcessor = new RecordProcessor(blockingQueue, concurrentMap);
        blockingQueue.add(t1);
        recordProcessor.processRecord();
        Assertions.assertThat(concurrentMap.containsKey(156108));
        Assertions.assertThat(concurrentMap.get(156108).isNotValid()).isTrue();
        Assertions.assertThat(concurrentMap.get(156108).getIrregularities().contains(Irregularities.BALANCE_NOT_VALID));
        System.out.println("sdfgdfg");
    }

    @Test
    public void testTransactionDuplicated() throws InterruptedException {
        BlockingQueue<Transaction> blockingQueue = new LinkedBlockingDeque<>(100);
        Transaction trDuplicated2 = Transaction.builder()
                .transactionReference(112806)
                .iban("NL93ABNA0585619023")
                .description("Duplicated-2")
                .startBalance(Double.parseDouble("52.21"))
                .mutation(Double.parseDouble("-33.21"))
                .endBalance(Double.parseDouble("19"))
                .isNotValid(false)
                .duplicatedTransactions(new ArrayList<>())
                .irregularities(new ArrayList<>())
                .build();
        blockingQueue.add(trDuplicated2);
        Map<Integer, Transaction> concurrentMap = new ConcurrentHashMap<>();
        Transaction trDuplicatedAlreadyProcessed = Transaction.builder()
                .transactionReference(112806)
                .iban("NL93ABNA0585619023")
                .description("Duplicated-1")
                .startBalance(Double.parseDouble("77.29"))
                .mutation(Double.parseDouble("-23.99"))
                .endBalance(Double.parseDouble("63.3"))
                .isNotValid(true)
                .duplicatedTransactions(new ArrayList<>())
                .irregularities(Arrays.asList(Irregularities.DUPLICATED_REF_NUMB))
                .build();
        concurrentMap.put(112806, trDuplicatedAlreadyProcessed);

        RecordProcessor recordProcessor = new RecordProcessor(blockingQueue, concurrentMap);

        recordProcessor.processRecord();
        Assertions.assertThat(concurrentMap.containsKey(112806));
        Assertions.assertThat(concurrentMap.get(112806).isNotValid()).isTrue();
        Assertions.assertThat(concurrentMap.get(112806).getIrregularities().contains(Irregularities.DUPLICATED_REF_NUMB));
        Assertions.assertThat(concurrentMap.get(112806).getDuplicatedTransactions()).hasSize(1);
        Assertions.assertThat(concurrentMap.get(112806).getDuplicatedTransactions().get(0).isNotValid());
        Assertions.assertThat(concurrentMap.get(112806).getDuplicatedTransactions().get(0).getIrregularities().contains(Irregularities.DUPLICATED_REF_NUMB));
        System.out.println("sdfgdfg");
    }

    @Test
    public void testTransactionDuplicatedAndBalanceNotValid() throws InterruptedException {
        BlockingQueue<Transaction> blockingQueue = new LinkedBlockingDeque<>(100);
        Transaction trDuplicated2 = Transaction.builder()
                .transactionReference(112806)
                .iban("NL93ABNA0585619023")
                .description("Duplicated-2")
                .startBalance(Double.parseDouble("52.21"))
                .mutation(Double.parseDouble("-33.21"))
                .endBalance(Double.parseDouble("20"))
                .isNotValid(false)
                .duplicatedTransactions(new ArrayList<>())
                .irregularities(new ArrayList<>())
                .build();
        blockingQueue.add(trDuplicated2);
        Map<Integer, Transaction> concurrentMap = new ConcurrentHashMap<>();
        Transaction trDuplicatedAlreadyProcessed = Transaction.builder()
                .transactionReference(112806)
                .iban("NL93ABNA0585619023")
                .description("Duplicated-1")
                .startBalance(Double.parseDouble("77.29"))
                .mutation(Double.parseDouble("-23.99"))
                .endBalance(Double.parseDouble("63.3"))
                .isNotValid(true)
                .duplicatedTransactions(new ArrayList<>())
                .irregularities(Arrays.asList(Irregularities.DUPLICATED_REF_NUMB))
                .build();
        concurrentMap.put(112806, trDuplicatedAlreadyProcessed);
        RecordProcessor recordProcessor = new RecordProcessor(blockingQueue, concurrentMap);

        recordProcessor.processRecord();
        Assertions.assertThat(concurrentMap.containsKey(112806));
        Assertions.assertThat(concurrentMap.get(112806).isNotValid()).isTrue();
        Assertions.assertThat(concurrentMap.get(112806).getIrregularities().contains(Irregularities.DUPLICATED_REF_NUMB));
        Assertions.assertThat(concurrentMap.get(112806).getDuplicatedTransactions()).hasSize(1);
        Assertions.assertThat(concurrentMap.get(112806).getDuplicatedTransactions().get(0).isNotValid());
        Assertions.assertThat(concurrentMap.get(112806).getDuplicatedTransactions().get(0).getIrregularities().containsAll(Arrays.asList(Irregularities.DUPLICATED_REF_NUMB, Irregularities.BALANCE_NOT_VALID)));
        System.out.println("sdfgdfg");
    }

}