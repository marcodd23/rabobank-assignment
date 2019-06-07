package io.sytac.rabobank.app.services;

import io.sytac.rabobank.app.model.ReportItem;
import io.sytac.rabobank.app.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportGenerator {

    private Map<Integer, Transaction> concurrentMap;

    @Autowired
    public ReportGenerator(Map<Integer, Transaction> concurrentMap) {
        this.concurrentMap = concurrentMap;
    }

    /**
     * This method will iterate the cuncurrentMap, will filter the invalid, and then it will map the invalid
     * transaction to a @{@link ReportItem} and add it to a List. If the Transaction Ref Number is duplicated,
     * it will map all the duplicated transactions to the List of @{@link ReportItem}
     *
     * @return
     */
    public List<ReportItem> generateReport() {
        List<ReportItem> report = new ArrayList<>();
        concurrentMap.entrySet().stream()
                .filter(transactionEntry -> transactionEntry.getValue().isNotValid())
                .forEach(transactionEntry -> {
                    Transaction transaction = transactionEntry.getValue();
                    report.add(mapTransactionToReportItem(transaction));
                    if (transaction.isDuplicated()) {
                        transaction.getDuplicatedTransactions().forEach(duplicatedTransaction -> {
                            report.add(mapTransactionToReportItem(duplicatedTransaction));
                        });
                    }
                });

        return report;
    }


    private ReportItem mapTransactionToReportItem(Transaction transaction) {
        ReportItem reportItem = new ReportItem();
        reportItem.setTransactionReference(transaction.getTransactionReference());
        reportItem.setDescription(transaction.getDescription());
        reportItem.setIrregularities(transaction.getIrregularities());
        return reportItem;
    }
}
