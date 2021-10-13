package com.store.storesharedmodule.constants;

public enum EventsChannels {
    
    AUTH_LOGIN_CHANNEL("auth.login.channel");

    private final String eventKey;

    EventsChannels(final String eventKey) {
        this.eventKey = eventKey;
    }

    public String getEventKey() {
        return this.eventKey;
    }

}
