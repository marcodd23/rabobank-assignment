package io.sytac.rabobank.app.services;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import io.sytac.rabobank.app.model.Transaction;
import io.sytac.rabobank.app.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;


@Service
@Slf4j
public class CsvParser {

    private CSVReader csvReader;
/*    private CSVReaderBuilder csvReaderBuilder;
    private String fileName;
    private final String basePath = "input/";*/

    @Autowired
    private BlockingQueue<Transaction> blockingQueue;

    private CsvToBean configure(String fileName){
        /*this.fileName = fileName;*/

        Reader reader = FileUtil.getReader(fileName);
        if (reader != null) {
            csvReader = new CSVReaderBuilder(reader)
                    .withSkipLines(1)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    .build();
        }
        ColumnPositionMappingStrategy mappingStrategy =
                new ColumnPositionMappingStrategy();

        mappingStrategy.setType(Transaction.class);
        //Fields in Transaction Bean
        String[] columns = new String[]{"transactionReference","iban","description","startBalance", "mutation", "endBalance"};
        //Setting the colums for mappingStrategy
        mappingStrategy.setColumnMapping(columns);
        //create instance for CsvToBean class
        CsvToBean ctb = new CsvToBean();
        ctb.setCsvReader(csvReader);
        ctb.setMappingStrategy(mappingStrategy);
        return ctb;
    }


    public void parseDocument(String fileName){
        CsvToBean ctb = configure(fileName);
        Iterator iterator = ctb.iterator();
        while (iterator.hasNext()){
            Transaction next = (Transaction) iterator.next();
            blockingQueue.add(next);
            log.info("##### Executing" + Thread.currentThread().getName());
        }

        try {
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
