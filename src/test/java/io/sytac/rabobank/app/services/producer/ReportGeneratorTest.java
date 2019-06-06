package io.sytac.rabobank.app.services.producer;

import io.sytac.rabobank.app.model.Irregularities;
import io.sytac.rabobank.app.model.ReportItem;
import io.sytac.rabobank.app.model.Transaction;
import io.sytac.rabobank.app.services.ReportGenerator;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportGeneratorTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void generateReportTest() {
        ReportGenerator reportGenerator = new ReportGenerator(generateMap());
        List<ReportItem> reportGenerated = reportGenerator.generateReport();
        Assertions.assertThat(reportGenerated).isNotEmpty();

        Assertions.assertThat(reportGenerated.get(0).getTransactionReference()).isEqualTo(112806);
        Assertions.assertThat(reportGenerated.get(0).getDescription()).isEqualTo("Duplicated-1");
        Assertions.assertThat(reportGenerated.get(0).getIrregularities()).hasSize(2);
        Assertions.assertThat(reportGenerated.get(0).getIrregularities()).contains(Irregularities.BALANCE_NOT_VALID, Irregularities.DUPLICATED_REF_NUMB);


        Assertions.assertThat(reportGenerated.get(1).getTransactionReference()).isEqualTo(112806);
        Assertions.assertThat(reportGenerated.get(1).getDescription()).isEqualTo("Duplicated-2");
        Assertions.assertThat(reportGenerated.get(1).getIrregularities()).hasSize(1);
        Assertions.assertThat(reportGenerated.get(1).getIrregularities()).contains(Irregularities.DUPLICATED_REF_NUMB);

        Assertions.assertThat(reportGenerated.get(2).getTransactionReference()).isEqualTo(112806);
        Assertions.assertThat(reportGenerated.get(2).getDescription()).isEqualTo("Duplicated-3");
        Assertions.assertThat(reportGenerated.get(2).getIrregularities()).hasSize(1);
        Assertions.assertThat(reportGenerated.get(2).getIrregularities()).contains(Irregularities.DUPLICATED_REF_NUMB);

        Assertions.assertThat(reportGenerated.get(3).getTransactionReference()).isEqualTo(147132);
        Assertions.assertThat(reportGenerated.get(3).getDescription()).isEqualTo("Subscription for Richard Dekker");
        Assertions.assertThat(reportGenerated.get(3).getIrregularities()).hasSize(1);
        Assertions.assertThat(reportGenerated.get(3).getIrregularities()).contains(Irregularities.BALANCE_NOT_VALID);
    }

    private Map<Integer, Transaction> generateMap(){

        Map<Integer, Transaction> concurrentMap = new ConcurrentHashMap<>();

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

        Transaction t2 = Transaction.builder()
                .transactionReference(147132)
                .iban("NL56RABO0149876948")
                .description("Subscription for Richard Dekker")
                .startBalance(Double.parseDouble("103.65"))
                .mutation(Double.parseDouble("+2.58"))
                .endBalance(Double.parseDouble("206.23"))
                .isNotValid(true)
                .duplicatedTransactions(new ArrayList<>())
                .irregularities(Arrays.asList(Irregularities.BALANCE_NOT_VALID))
                .build();


        Transaction trDuplicated1 = Transaction.builder()
                .transactionReference(112806)
                .iban("NL93ABNA0585619023")
                .description("Duplicated-1")
                .startBalance(Double.parseDouble("77.29"))
                .mutation(Double.parseDouble("-23.99"))
                .endBalance(Double.parseDouble("63.3"))
                .isNotValid(true)
                .duplicatedTransactions(new ArrayList<>())
                .irregularities(Arrays.asList(Irregularities.BALANCE_NOT_VALID, Irregularities.DUPLICATED_REF_NUMB))
                .build();

        Transaction trDuplicated2 = Transaction.builder()
                .transactionReference(112806)
                .iban("NL93ABNA0585619023")
                .description("Duplicated-2")
                .startBalance(Double.parseDouble("52.21"))
                .mutation(Double.parseDouble("-33.21"))
                .endBalance(Double.parseDouble("19"))
                .isNotValid(true)
                .duplicatedTransactions(new ArrayList<>())
                .irregularities(Arrays.asList(Irregularities.DUPLICATED_REF_NUMB))
                .build();

        Transaction trDuplicated3 = Transaction.builder()
                .transactionReference(112806)
                .iban("NL93ABNA0585619023")
                .description("Duplicated-3")
                .startBalance(Double.parseDouble("39.9"))
                .mutation(Double.parseDouble("-42.57"))
                .endBalance(Double.parseDouble("-2.67"))
                .isNotValid(true)
                .duplicatedTransactions(new ArrayList<>())
                .irregularities(Arrays.asList(Irregularities.DUPLICATED_REF_NUMB))
                .build();

        trDuplicated1.setDuplicatedTransactions(Arrays.asList(trDuplicated2, trDuplicated3));

        concurrentMap.put(t1.getTransactionReference(), t1);
        concurrentMap.put(t2.getTransactionReference(), t2);
        concurrentMap.put(trDuplicated1.getTransactionReference(), trDuplicated1);

        return concurrentMap;
    }
}