package io.sytac.rabobank.app.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Scope("prototype")
public class BatchJob  implements Runnable{

    @Override
    public void run() {

    }
}
