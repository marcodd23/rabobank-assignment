package io.sytac.rabobank.app.services;

import io.sytac.rabobank.app.model.Transaction;
import io.sytac.rabobank.app.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.*;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

@Service
@Slf4j
public class XmlParser {

    @Autowired
    private BlockingQueue<Transaction> blockingQueue;

    public void parseDocument(String fileName) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(FileUtil.getReader(fileName));
            Transaction transaction = null;
            String elementValue = null;
            while (eventReader.hasNext()){
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT: {
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        if (qName.equalsIgnoreCase("record")) {
                            transaction = new Transaction();
                            Iterator<Attribute> attributes = startElement.getAttributes();
                            if (attributes.hasNext()) {
                                Attribute attr = attributes.next();
                                Integer referenceNumb = Integer.parseInt(attr.getValue());
                                transaction.setTransactionReference(referenceNumb);
                            }
                        }
                        break;
                    }
                    case XMLStreamConstants.CHARACTERS:
                    {
                        Characters characters = event.asCharacters();
                        elementValue = characters.getData();
                        break;
                    }
                    case XMLStreamConstants.END_ELEMENT:
                    {
                        EndElement endElement = event.asEndElement();
                        String qName = endElement.getName().getLocalPart();
                        if (transaction != null && qName.equalsIgnoreCase("accountNumber")) {
                            transaction.setIban(elementValue);
                        }
                        if (transaction != null && qName.equalsIgnoreCase("description")) {
                            transaction.setDescription(elementValue);
                        }

                        if (transaction != null && qName.equalsIgnoreCase("startBalance")) {
                            transaction.setStartBalance(Double.parseDouble(elementValue));
                        }
                        if (transaction != null && qName.equalsIgnoreCase("mutation")) {
                            transaction.setMutation(Double.parseDouble(elementValue));
                        }
                        if (transaction != null && qName.equalsIgnoreCase("mutation")) {
                            transaction.setMutation(Double.parseDouble(elementValue));
                        }
                        if (transaction != null && qName.equalsIgnoreCase("endBalance")) {
                            transaction.setEndBalance(Double.parseDouble(elementValue));
                            blockingQueue.add(transaction);
                        }
                        break;
                    }
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }


}
