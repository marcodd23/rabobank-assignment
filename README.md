# rabobank-assignment
The application is a Batch processor that use the PRODUCER - CONSUMER pattern.
The Producer works in the main thread, and process the file that you set in the input.files properties record by record and insert the processed records
in a BlockingQueue.
The consumer works by default on 2 thread (but the number is configurable with the --consumer.treads paramenter). 
Each thread process a transaction records and put it in a concurrent hash map.
When the files has been read till the end, the main thread process the hashMap to check the duplicates 
The batch will generate a report.json file in the same directory where the jar file is located 

## To run with Maven:
- mvn spring-boot:run -Dspring-boot.run.arguments=--consumer.treads=3

## To run with Java:
- java -jar app-0.0.1-SNAPSHOT.jar --consumer.treads=5


# Paramenters: 

- consumer.treads: Number of threads to use for the consumer


