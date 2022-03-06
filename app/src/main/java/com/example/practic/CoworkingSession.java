package com.example.practic;

import java.sql.Date;

public class CoworkingSession {
    private CoworkerSpace space;
    private String qr;
    private String purpose;
    private Date startSession;
    private Date endSession;

    public CoworkingSession(CoworkerSpace space,
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


    public CoworkerSpace getSpace() {
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
