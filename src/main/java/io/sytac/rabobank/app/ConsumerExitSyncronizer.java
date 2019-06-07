package io.sytac.rabobank.app;

import org.springframework.stereotype.Component;

@Component
public class ConsumerExitSyncronizer {
    private volatile Boolean notifyConsumersShutdown = false;

    public synchronized Boolean getNotifyConsumersShutdown() {
        return notifyConsumersShutdown;
    }

    public synchronized void setNotifyConsumersShutdown(Boolean notifyConsumersShutdown) {
        this.notifyConsumersShutdown = notifyConsumersShutdown;
    }
}
