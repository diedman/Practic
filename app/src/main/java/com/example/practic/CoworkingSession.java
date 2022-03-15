package com.example.practic;

import java.sql.Date;

public class CoworkingSession {
    private CoworkingSpace space;
    private String qr;
    private String purpose;
    private Date startSession;
    private Date endSession;

    public CoworkingSession(CoworkingSpace space,
                            String qr,
                            String purpose,
                            Date startSession,
                            Date endSession) {
        this.space = space;
        this.qr = qr;
        this.purpose = purpose;
        this.startSession = startSession;
        this.endSession = endSession;
    }


    public CoworkingSpace getSpace() {
        return space;
    }

    public String getQr() {
        return qr;
    }

    public String getPurpose() {
        return purpose;
    }

    public Date getStartSession() {
        return startSession;
    }

    public Date getEndSession() {
        return endSession;
    }
}
