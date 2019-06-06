package io.sytac.rabobank.app.services.producer;

import io.sytac.rabobank.app.model.Transaction;
import io.sytac.rabobank.app.services.producer.XmlParser;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@RunWith(MockitoJUnitRunner.class)
public class XmlParserTest {

    private BlockingQueue<Transaction> blockingQueue = new LinkedBlockingDeque<>(100);

    @Test
    public void testAllTransactionsArePresent() throws InterruptedException {
        XmlParser xmlParser = new XmlParser(blockingQueue);
        xmlParser.parseDocument("records.xml");
        Assertions.assertThat(blockingQueue).hasSize(3);
    }

    @Test
    public void testTransactionMappedCorrectly() throws InterruptedException {
        XmlParser xmlParser = new XmlParser(blockingQueue);
        xmlParser.parseDocument("records2.xml");
        Assertions.assertThat(blockingQueue).hasSize(1);
        Transaction transaction = blockingQueue.take();
        Assertions.assertThat(transaction.getTransactionReference()).isEqualTo(164702);
        Assertions.assertThat(transaction.getIban()).isEqualTo("NL46ABNA0625805417");
        Assertions.assertThat(transaction.getDescription()).isEqualTo("Flowers for Rik Dekker");
        Assertions.assertThat(transaction.getStartBalance()).isEqualTo(Double.parseDouble("81.89"));
        Assertions.assertThat(transaction.getMutation()).isEqualTo(Double.parseDouble("+5.99"));
        Assertions.assertThat(transaction.getEndBalance()).isEqualTo(Double.parseDouble("87.88"));
    }
}