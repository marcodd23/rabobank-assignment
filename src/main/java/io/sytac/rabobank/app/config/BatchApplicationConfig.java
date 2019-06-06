package io.sytac.rabobank.app.config;

import io.sytac.rabobank.app.model.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.*;

@Configuration
@EnableAsync
public class BatchApplicationConfig {


    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setThreadNamePrefix("consumer-Tread");
        executor.initialize();
        return executor;
    }

    @Bean("executorService")
    public Executor executorService(){
        return Executors.newCachedThreadPool();
    }

    @Bean(name = "blockingQueue")
    public BlockingQueue<Transaction> getBlockingQueue(){
        return new LinkedBlockingDeque<>(100);

    }

/*    @Bean(name = "concurrentMap")
    public Map<Integer, List<Transaction>> getConcurrentMap(){
        return new ConcurrentHashMap<>();
    }*/

    @Bean(name = "concurrentMap")
    public Map<Integer, Transaction> getConcurrentMap(){
        return new ConcurrentHashMap<>();
    }

/*    @Bean(name = "producerConsumerQueue")
    public ProducerConsumerQueue getProducerConsumerQueue(){
        return new ProducerConsumerQueue(100);
    }*/
}
