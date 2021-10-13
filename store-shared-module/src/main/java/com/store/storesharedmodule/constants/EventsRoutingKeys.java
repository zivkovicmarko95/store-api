package com.store.storesharedmodule.constants;

public enum EventsRoutingKeys {
    
    AUTH_LOGIN_ROUTING_KEY("auth.login.routing_key");

    private final String key;

    EventsRoutingKeys(final String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

}
