package io.sytac.rabobank.app.services.producer;

import io.sytac.rabobank.app.ConsumerExitSyncronizer;
import io.sytac.rabobank.app.services.CsvParser;
import io.sytac.rabobank.app.services.XmlParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProducerJob {

    @Autowired
    private CsvParser csvParser;
    @Autowired
    private XmlParser xmlParser;
    @Autowired
    private ConsumerExitSyncronizer consumerExitSyncronizer;

    private static final String CSV_EXTENSION = "csv";
    private static final String XML_EXTENSION = "xml";

    public void produce(String filename){
        String extension = Optional.ofNullable(StringUtils.split(filename, ".")).map(strings -> strings[1]).orElse(null);
        if(StringUtils.isNotBlank(extension)){
            if(extension.equals(CSV_EXTENSION)){
                csvParser.parseDocument(filename);
            }else if(extension.equals(XML_EXTENSION)){
                xmlParser.parseDocument(filename);
            }
        }
/*        String[] filenameSplitted = StringUtils.split(filename, ".");
        String extension = "";
        if(filenameSplitted.length == 2){
            extension = filenameSplitted
        }*/
        //csvParser.parseDocument(filename);
        consumerExitSyncronizer.setNotifyConsumersShutdown(true);
    }
}
