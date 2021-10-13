package com.store.storesharedmodule.constants;

public enum EventsQueues {
    
    AUTH_LOGIN_QUEUE("auth.login.queue");

    private final String key;

    EventsQueues(final String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
