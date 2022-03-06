package com.example.practic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatabaseAPITest {
    private static Connection conn = DBUtil.changeDatabase("TestPractic");

    private static void clearTable(String tableName) {
        try {
            String query = "DELETE FROM " + tableName + ";";
            conn.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertDefaultCoworker() {
        String firstname     = "Иван";
        String lastName      = "Иванов";
        String patronymic    = "Иванович";
        String password      = "12345678";
        Date birthday        = null;
        try {
            birthday         = new java.sql.Date(Objects.requireNonNull(new SimpleDateFormat("dd.MM.yyyy")
                    .parse("12.12.2000")).getTime()) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String email         = "some@mail.ru";
        String phoneNum      = "88005553535";
        String maritalStatus = "не замужем/не женат";

        DBCommunication.registerCoworker(firstname, lastName, patronymic, password,
                birthday, email, phoneNum, maritalStatus);

        try {
            String query = "SELECT id FROM coworkers WHERE email = ?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                CoworkerData.id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void registerOnCoworking(int idSpace) {
        int idCoworker = CoworkerData.id;
        String qr      = "sliyfgouiwbrnlksjliufvuads";
        Date startDate = new Date(0);
        Date endDate   = new Date(0);
        try {
            startDate  = new java.sql.Date(Objects.requireNonNull(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                    .parse("12.01.2022 12:00:00")).getTime());

            endDate    = new java.sql.Date(Objects.requireNonNull(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                    .parse("12.01.2022 16:00:00")).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String purpose = "самостоятельная работа";

        String[] tools = new String[3];
        tools[0] = "ноутбук";
        tools[1] = "флипчарт";
        tools[2] = "ламинатор";

        DBCommunication.registerOnCoworking(idSpace, idCoworker, qr,
                startDate, endDate, purpose, tools);
    }

    @Test
    public void checkCoworkerRegistration() {
        clearTable("session_tools");
        clearTable("coworkers_events");
        clearTable("coworkers_spaces");
        clearTable("coworkers");

        String firstname     = "Иван";
        String lastName      = "Иванов";
        String patronymic    = "Иванович";
        String password      = "12345678";
        Date birthday        = null;
        try {
            birthday         = new java.sql.Date(Objects.requireNonNull(new SimpleDateFormat("dd.MM.yyyy")
                    .parse("12.12.2000")).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String email         = "some";
        String phoneNum      = "88005553535";
        String maritalStatus = "не замужем/не женат";

        assertEquals(DBCommunication.registerCoworker(firstname, lastName, patronymic, password,
                birthday, email, phoneNum, maritalStatus), 1);

        patronymic = null;
        email = "someanother";

        assertEquals(DBCommunication.registerCoworker(firstname, lastName, patronymic, password,
                birthday, email, phoneNum, maritalStatus), 1);

        assertEquals(DBCommunication.registerCoworker(firstname, lastName, patronymic, password,
                birthday, email, phoneNum, maritalStatus), 0);
    }

    @Test
    public void checkAuthentication() {
        clearTable("session_tools");
        clearTable("coworkers_events");
        clearTable("coworkers_spaces");
        clearTable("coworkers");

        insertDefaultCoworker();

        assertEquals(DBCommunication.authenticateCoworker("some@mail.ru", "12345678"), 1);
        assertEquals(DBCommunication.authenticateCoworker("some@mail.ru", "23456789"), 0);
        assertEquals(DBCommunication.authenticateCoworker("another@mail.ru", "12345678"), 0);
    }

    @Test
    public void checkRegistrationOnCoworking() {
        clearTable("session_tools");
        clearTable("coworkers_spaces");
        clearTable("coworkers");

        insertDefaultCoworker();

        int idCoworker = CoworkerData.id;
        int idSpace    = 1;
        String qr      = "sliyfgouiwbrnlksjliufvuads";
        Date startDate = new Date(0);
        Date endDate   = new Date(0);
        try {
            startDate  = new java.sql.Date(Objects.requireNonNull(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                    .parse("12.01.2022 12:00:00")).getTime());

            endDate    = new java.sql.Date(Objects.requireNonNull(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                    .parse("12.01.2022 16:00:00")).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String purpose = "самостоятельная работа";
        String[] tools = new String[1];
        tools[0] = "ноутбук";

        assertEquals(DBCommunication.registerOnCoworking(idSpace, idCoworker, qr,
                                                         startDate, endDate, purpose,
                                                         tools), 1);

        assertEquals(DBCommunication.registerOnCoworking(idSpace, idCoworker, qr,
                startDate, endDate, purpose,
                tools), -1);
    }

    @Test
    public void checkRegistrationOnEvent() {
        clearTable("session_tools");
        clearTable("coworkers_events");
        clearTable("coworkers_spaces");
        clearTable("coworkers");

        insertDefaultCoworker();
        registerOnCoworking(1);

        int idCoworker = CoworkerData.id;
        int idEvent    = 1;
        String qr      = "jowiugfsjksnsjfhisf";

        assertEquals(DBCommunication.registerOnEvent(idEvent, idCoworker, qr), 1);
        assertEquals(DBCommunication.getEventQR(1, idCoworker), "jowiugfsjksnsjfhisf");
    }

    @Test
    public void checkRegistrationTools() {
        clearTable("session_tools");
        clearTable("coworkers_events");
        clearTable("coworkers_spaces");
        clearTable("coworkers");

        insertDefaultCoworker();

        int idCoworker = CoworkerData.id;
        int idSpace    = 1;
        String qr      = "iouwirsfhkashfgaoiuweguds";
        Date startDate = new Date(0);
        Date endDate   = new Date(0);
        try {
            startDate  = new java.sql.Date(Objects.requireNonNull(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                    .parse("12.01.2022 12:00:00")).getTime());

            endDate    = new java.sql.Date(Objects.requireNonNull(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                    .parse("12.01.2022 16:00:00")).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String purpose = "самостоятельная работа";

        String[] tools = new String[3];
        tools[0] = "ноутбук";
        tools[1] = "флипчарт";
        tools[2] = "ламинатор";

        DBCommunication.registerOnCoworking(idSpace, idCoworker, qr, startDate, endDate, purpose, tools);

        assertEquals(DBCommunication.registerTools(idCoworker, idSpace, tools), 1);

        qr = "hxiotifuygkjeljwidsfyvig";
        idSpace = 2;
        tools[1] = "планшет";
        DBCommunication.registerOnCoworking(idSpace, idCoworker, qr, startDate, endDate, purpose, tools);
        assertEquals(DBCommunication.registerTools(idCoworker, idSpace, tools), -1);
    }

    @Test
    public void checkCoworkerSession() {
        clearTable("session_tools");
        clearTable("coworkers_events");
        clearTable("coworkers_spaces");
        clearTable("coworkers");


        insertDefaultCoworker();
        registerOnCoworking(1);

        List<CoworkingSession> sessions = new ArrayList<>();
        Date startDate = null;
        Date endDate   = null;
        try {
        startDate  = new java.sql.Date(Objects.requireNonNull(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                .parse("12.01.2022 12:00:00")).getTime());

        endDate    = new java.sql.Date(Objects.requireNonNull(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                .parse("12.01.2022 16:00:00")).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sessions.add(new CoworkingSession(new CoworkerSpace(1, "пр. Медиков, д.3",
                                          Coordinates.parse("59.969346937019544:30.315582369860376")),
                     "sliyfgouiwbrnlksjliufvuads", "самостоятельная работа", startDate, endDate));

        List<CoworkingSession> coworkingSessions = DBCommunication.getCoworkerSessions(CoworkerData.id);
        assertEquals(coworkingSessions.get(0).getSpace().getId(),
                     sessions.get(0).getSpace().getId());
        assertEquals(coworkingSessions.get(0).getSpace().getTitle(),
                     sessions.get(0).getSpace().getTitle());
        assertEquals(coworkingSessions.get(0).getSpace().getCoordinates().latitude,
                     sessions.get(0).getSpace().getCoordinates().latitude, 0.000001);
        assertEquals(coworkingSessions.get(0).getSpace().getCoordinates().longitude,
                     sessions.get(0).getSpace().getCoordinates().longitude, 0.000001);
        assertEquals(coworkingSessions.get(0).getStartSession().toString(),
                     sessions.get(0).getStartSession().toString());
        assertEquals(coworkingSessions.get(0).getEndSession().toString(),
                     sessions.get(0).getEndSession().toString());
        assertEquals(coworkingSessions.get(0).getQr(),
                     sessions.get(0).getQr());
        assertEquals(coworkingSessions.get(0).getPurpose(),
                     sessions.get(0).getPurpose());

        registerOnCoworking(2);

        sessions.add(new CoworkingSession(new CoworkerSpace(2, "пр. Непокоренных, д. 16",
                Coordinates.parse("59.99640477714888:30.382536956557903")),
                "sliyfgouiwbrnlksjliufvuads", "самостоятельная работа", startDate, endDate));

        coworkingSessions = DBCommunication.getCoworkerSessions(CoworkerData.id);
        assertEquals(coworkingSessions.get(1).getSpace().getId(),
                     sessions.get(1).getSpace().getId());
        assertEquals(coworkingSessions.get(1).getSpace().getTitle(),
                     sessions.get(1).getSpace().getTitle());
        assertEquals(coworkingSessions.get(1).getSpace().getCoordinates().latitude,
                     sessions.get(1).getSpace().getCoordinates().latitude, 0.000001);
        assertEquals(coworkingSessions.get(1).getSpace().getCoordinates().longitude,
                     sessions.get(1).getSpace().getCoordinates().longitude, 0.000001);
        assertEquals(coworkingSessions.get(1).getStartSession().toString(),
                     sessions.get(1).getStartSession().toString());
        assertEquals(coworkingSessions.get(1).getEndSession().toString(),
                     sessions.get(1).getEndSession().toString());
        assertEquals(coworkingSessions.get(1).getQr(),
                     sessions.get(1).getQr());
        assertEquals(coworkingSessions.get(1).getPurpose(),
                     sessions.get(1).getPurpose());
    }

}