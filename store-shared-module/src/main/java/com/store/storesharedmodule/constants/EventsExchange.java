package com.store.storesharedmodule.constants;

public enum EventsExchange {
    
    GLOBAL_EXCHANGE("GlobalExchange");

    private final String key;

    EventsExchange(final String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

}
