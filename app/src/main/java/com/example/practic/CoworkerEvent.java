package com.example.practic;

public class CoworkerEvent {
    private EventData event;
    private String qr;

    CoworkerEvent(EventData event, String qr) {
        this.event = event;
        this.qr    = qr;
    }

    public EventData getEvent() {
        return event;
    }

    public String getQr() {
        return qr;
    }
}
