package io.sytac.rabobank.app;

import io.sytac.rabobank.app.services.ReportGenerator;
import io.sytac.rabobank.app.services.consumer.ConsumerJob;
import io.sytac.rabobank.app.services.producer.CsvParser;
import io.sytac.rabobank.app.services.producer.ProducerJob;
import io.sytac.rabobank.app.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
@Slf4j
public class BatchApplication implements CommandLineRunner {

	@Autowired
	private CsvParser csvParser;
	@Autowired
	private ConsumerJob consumerJob;
	@Autowired
	private ProducerJob producerJob;
	@Autowired
	private Map<Integer,Transaction> concurrentMap;
	@Autowired
	private ReportGenerator reportGenerator;

	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		consumerJob.processRecord();
		consumerJob.processRecord();

		producerJob.produce("records.csv");
		//csvParser.parseDocument("records.csv");

		System.out.println("COMPLETATO !!!!");

		reportGenerator.generateReport();

		System.exit(0);
	}
}
