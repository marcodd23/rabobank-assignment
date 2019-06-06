package io.sytac.rabobank.app.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.sytac.rabobank.app.model.ReportItem;
import io.sytac.rabobank.app.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportGenerator {

    @Autowired
    private Map<Integer, Transaction> concurrentMap;

    public void generateReport() {
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

        writeReportToJsonFile(report);
    }

    private void writeReportToJsonFile(List<ReportItem> report) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fileWriter = new FileWriter(new File("jsonReport.json"));
            gson.toJson(report, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            log.error("#### - Error priting report!!");
            e.printStackTrace();
        }
    }


    private ReportItem mapTransactionToReportItem(Transaction transaction) {
        ReportItem reportItem = new ReportItem();
        reportItem.setTransactionReference(transaction.getTransactionReference());
        reportItem.setDescription(transaction.getDescription());
        reportItem.setIrregularities(transaction.getIrregularities());
        return reportItem;
    }
}
