package io.sytac.rabobank.app.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.sytac.rabobank.app.model.ReportItem;
import io.sytac.rabobank.app.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ReportGenerator {

    @Autowired
    private Map<Integer, Transaction> concurrentMap;

    public void generateReport() throws IOException {
        List<ReportItem> report = new ArrayList<>();
                concurrentMap.entrySet().stream()
                .filter(transactionEntry -> transactionEntry.getValue().isNotValid())
                .forEach(transactionEntry -> {
                    Transaction transaction = transactionEntry.getValue();
                    report.add(mapTransactionToReportItem(transaction));
                    if(transaction.isDuplicated()){
                        transaction.getDuplicatedTransactions().forEach(duplicatedTransaction -> {
                            report.add(mapTransactionToReportItem(duplicatedTransaction));
                        });
                    }
                });

/*        List<ReportItem> report = new ArrayList<>();
        concurrentMap.entrySet().stream()
                .filter(transactionEntry -> transactionEntry.getValue().isNotValid())
                .forEach(transactionEntry -> {
                    Transaction transaction = transactionEntry.getValue();
                    if(transaction.isDuplicated()){
                        report.add(mapTransactionToReportItem(transaction, true));
                        transaction.getDuplicatedTransactions().forEach(duplicatedTransaction -> {
                            report.add(mapTransactionToReportItem(duplicatedTransaction, true));
                        });
                    }else {
                        report.add(mapTransactionToReportItem(transaction, false));
                    }
                });*/


        Gson gson = new GsonBuilder().setPrettyPrinting().create();


        //String filePath = FileUtil.createFileUseJavaNewIO("jsonReport.json");

        FileWriter fileWriter = new FileWriter(new File("jsonReport2.json"));

        /*String jsonReport = */gson.toJson(report, fileWriter);

        fileWriter.flush();
        fileWriter.close();

        //System.out.println(jsonReport);
    }


    private ReportItem mapTransactionToReportItem(Transaction transaction){
        ReportItem reportItem = new ReportItem();
        reportItem.setTransactionReference(transaction.getTransactionReference());
        reportItem.setDescription(transaction.getDescription());
        reportItem.setIrregularities(transaction.getIrregularities());
        return reportItem;
    }
}
