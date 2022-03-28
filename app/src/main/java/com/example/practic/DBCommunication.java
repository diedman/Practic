package com.example.practic;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBCommunication {
    private static Connection conn = DBUtil.getConnection();

    public static int isCoworkerExists(String email) {
        int res = 0;
        try {
            String query = "SELECT * FROM coworkers WHERE email = ?;";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            res = rs.next() ? 1 : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

//    public static int loadCoworker(int idCoworker) {
//        int res = -1;
//        try {
//            String query = "SELECT * FROM ";
//        } catch ()
//    }

    public static int registerCoworker(String firstname,
                                       String lastName,
                                       String patronymic,
                                       String password,
                                       Date birthday,
                                       String email,
                                       String phoneNum,
                                       String sex,
                                       String maritalStatus) {
        int res = -1;

        try {
            if (isCoworkerExists(email) == 1) { return 0; }

            int idMaritalStatus = getMaritalStatusId(maritalStatus);

            if (idMaritalStatus == -1) {
                return -1;
            }

            String query = "INSERT INTO coworkers (firstname, lastname, patronymic, hashed_password, birthday, email, phone_num, sex, id_marital_status) \n" +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

            String hashedPassword = PasswordAuthentication.getHashedPassword(password);

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, firstname);
            stmt.setString(2, lastName);
            stmt.setString(3, patronymic);
            stmt.setString(4, hashedPassword);
            stmt.setDate(5, birthday);
            stmt.setString(6, email);
            stmt.setString(7, phoneNum);
            stmt.setString(8, sex);
            stmt.setInt(9, idMaritalStatus);

            stmt.executeUpdate();
            res = 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private static int deleteRegistrationIfExists(int idSpace, int idCoworker, Date regDate) {
        int res = -1;
        try {
            clearTools(idCoworker, idSpace);
            Date dateAfterRegDate = addDays(regDate, 1);
            String query = "DELETE FROM coworkers_spaces \n" +
                    "WHERE (id_space = ?) AND (id_coworker >= ?) AND " +
                    "(start_session >= ?) AND (start_session < ?);";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSpace);
            stmt.setInt(2, idCoworker);
            stmt.setDate(3, regDate);
            stmt.setDate(4, dateAfterRegDate);

            stmt.executeUpdate();
            res = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int registerOnCoworking(int idSpace,
                                          int idCoworker,
                                          String qr,
                                          Timestamp startSessionTimestamp,
                                          Timestamp endSessionTimestamp,
                                          String purposeName,
                                          String[] tools) {
        int res = -1;
        try {
            int delRes = deleteRegistrationIfExists(idSpace, idCoworker, new Date(startSessionTimestamp.getTime()));
            if (delRes == -1) {
                return -1;
            }
            String query = "INSERT INTO coworkers_spaces (id_space, id_coworker, qr, " +
                            "start_session, end_session, id_purpose) \n" +
                           "VALUES (?, ?, ?, ?, ?, ?);";

            int IdPurpose = getPurposeId(purposeName);

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSpace);
            stmt.setInt(2, idCoworker);
            stmt.setString(3, qr);
            stmt.setTimestamp(4, startSessionTimestamp);
            stmt.setTimestamp(5, endSessionTimestamp);
            stmt.setInt(6, IdPurpose);

            stmt.executeUpdate();

            res = registerTools(idCoworker, idSpace, tools);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int registerTools(int idCoworker, int idSpace, String[] tools) {
        int res = -1;
        clearTools(idCoworker, idSpace, tools);
        if (tools.length == 0) { return 1; }
        try {
            String query = "INSERT INTO session_tools (id_tool, id_coworker, id_space) \n" +
                    "VALUES (?, ?, ?);";
            PreparedStatement stmt = conn.prepareStatement(query);

            for (String tool: tools) {
                int idTool = getToolId(tool);

                if (idTool == -1) {
                    return -1;
                }

                stmt.setInt(1, idTool);
                stmt.setInt(2, idCoworker);
                stmt.setInt(3, idSpace);

                stmt.executeUpdate();
            }
            res = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int isAlreadyRegisteredOnEvent(int idEvent, int idCoworker) {
        int res = -1;
        try {
            String query = "SELECT * FROM coworkers_events WHERE (id_event = ?) AND (id_coworker = ?);";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, idEvent);
            stmt.setInt(2, idCoworker);

            ResultSet rs = stmt.executeQuery();
            res = rs.next() ? 1 : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int registerOnEvent(int idEvent, int idCoworker, String qr) {
        int res = -1;
        try {
            int checkRes = isAlreadyRegisteredOnEvent(idEvent, idCoworker);
            if (checkRes != 0) {
                if (checkRes == 1) {
                    return 0;
                } else {
                    return -1;
                }
            }
            String query = "INSERT INTO coworkers_events (id_event, id_coworker, qr) \n" +
                    "VALUES (?, ?, ?);";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, idEvent);
            stmt.setInt(2, idCoworker);
            stmt.setString(3, qr);

            stmt.executeUpdate();
            res = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int authenticateCoworker(String email, String password) {
        int res = -1;
        try {
            String query = "SELECT coworkers.id, coworkers.firstname, coworkers.lastname, " +
                           "coworkers.patronymic, coworkers.hashed_password, coworkers.birthday, " +
                           "coworkers.email, coworkers.phone_num, coworkers.sex, marital_statuses.title \n" +
                           "FROM coworkers, marital_statuses \n" +
                           "WHERE coworkers.email = ? " +
                           "AND coworkers.id_marital_status = marital_statuses.id;";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                CoworkerData.id             = rs.getInt(1);
                CoworkerData.firstname      = rs.getString(2);
                CoworkerData.lastname       = rs.getString(3);
                CoworkerData.patronymic     = rs.getString(4);
                String hashedPassword       = rs.getString(5);
                CoworkerData.birthday       = rs.getDate(6);
                CoworkerData.email          = rs.getString(7);
                CoworkerData.phoneNum       = rs.getString(8);
                CoworkerData.gender         = rs.getString(9);
                CoworkerData.maritalStatus  = rs.getString(10);

                res = PasswordAuthentication.authenticate(password, hashedPassword) ? 1 : 0;
            } else {
                res = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int getSpacesQuantity(int spaceId) {
        int res = 0;
        try {
            String query = "SELECT seats FROM spaces WHERE id = ?;";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, spaceId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static int getFreeSpacesQuantity(int spaceId, Timestamp startSessionTimestamp, Timestamp endSessionTimestamp) {
        int res = 0;
        try {
            String query = "SELECT COUNT(*) FROM coworkers_spaces\n" +
                           "WHERE ((start_session <= ? AND end_session >  ?) OR \n" +
                                  "(start_session <  ? AND end_session >= ?) OR\n" +
                                  "(start_session <= ? AND end_session >= ?) OR\n" +
                                  "(start_session >= ? AND end_session <= ?)) AND\n" +
                                  "(id_space = ?);";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setTimestamp(1, startSessionTimestamp);
            stmt.setTimestamp(2, startSessionTimestamp);
            stmt.setTimestamp(3, endSessionTimestamp);
            stmt.setTimestamp(4, endSessionTimestamp);
            stmt.setTimestamp(5, startSessionTimestamp);
            stmt.setTimestamp(6, endSessionTimestamp);
            stmt.setTimestamp(7, startSessionTimestamp);
            stmt.setTimestamp(8, endSessionTimestamp);
            stmt.setInt(9, spaceId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return getSpacesQuantity(spaceId) - res;
    }

    public static CoworkingSpace getCoworkingSpace(int spaceId) {
        CoworkingSpace space = null;
        try {
            String query = "SELECT * FROM spaces WHERE id = ?;";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, spaceId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int    id       = rs.getInt(1);
                String title    = rs.getString(2);
                String locality = rs.getString(3);
                int    seats    = rs.getInt(4);

                Coordinates coords = Coordinates.parse(locality);

                space = new CoworkingSpace(id, title, coords, seats);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return space;
    }

    public static List<CoworkingSpace> getCoworkingSpaces() {
        List<CoworkingSpace> spaces = new ArrayList<>();
        try {
            String query = "SELECT * FROM spaces;";

            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int    id       = rs.getInt(1);
                String title    = rs.getString(2);
                String locality = rs.getString(3);
                int    seats    = rs.getInt(4);

                Coordinates coords = Coordinates.parse(locality);

                spaces.add(new CoworkingSpace(id, title, coords, seats));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spaces;
    }

    public static List<CoworkingSession> getCoworkerSessions(int coworkerId) {
        List<CoworkingSession> spaces = new ArrayList<>();
        try {
            String query = "SELECT spaces.id, spaces.title, coworkers_spaces.qr, " +
                    "purposes.title, coworkers_spaces.start_session, coworkers_spaces.end_session," +
                    "spaces.locality, spaces.seats \n" +
                    "FROM coworkers_spaces, spaces, purposes \n" +
                    "WHERE (spaces.id = coworkers_spaces.id_space) " +
                    "AND (purposes.id = coworkers_spaces.id_purpose);";

            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int    idSpace      = rs.getInt(1);
                String spaceName    = rs.getString(2);
                String qr           = rs.getString(3);
                String purpose      = rs.getString(4);
                Date   startSession = rs.getDate(5);
                Date   endSession   = rs.getDate(6);
                String locality     = rs.getString(7);
                int    seats        = rs.getInt(8);

                Coordinates coords = Coordinates.parse(locality);

                CoworkingSpace space = new CoworkingSpace(idSpace, spaceName, coords, seats);

                spaces.add(new CoworkingSession(space, qr, purpose, startSession, endSession));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spaces;
    }

    public static List<EventData> getEvents() {
        List<EventData> events = new ArrayList<>();
        try {
            String query = "SELECT * FROM events;";

            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int    id         = rs.getInt(1);
                String title      = rs.getString(2);
                String eventDescr = rs.getString(3);
                Date   date       = rs.getDate(4);
                String speaker    = rs.getString(5);
                int    spaceId    = rs.getInt(6);

                CoworkingSpace space = getCoworkingSpace(spaceId);

                events.add(new EventData(id, title, eventDescr, date, speaker, space));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public static List<EventData> getCoworkingEvents(int idCoworkingSpace) {
        List<EventData> events = new ArrayList<>();
        try {
            String query = "SELECT * FROM events WHERE id_space = ?;";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, idCoworkingSpace);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int    id         = rs.getInt(1);
                String title      = rs.getString(2);
                String eventDescr = rs.getString(3);
                Date   date       = rs.getDate(4);
                String speaker    = rs.getString(5);
                int    spaceId    = rs.getInt(6);

                CoworkingSpace space = getCoworkingSpace(spaceId);

                events.add(new EventData(id, title, eventDescr, date, speaker, space));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public static List<CoworkerEvent> getEventsOfCoworker(int coworkerId) {
        List<CoworkerEvent> events = new ArrayList<>();
        try {
            String query = "SELECT events.id, events.title, events.event_descr, events.meeting_date, " +
                    "events.speaker, events.id_space, coworkers_events.qr\n" +
                    "FROM events, coworkers_events \n" +
                    "WHERE (events.id = coworkers_events.id_event) AND (coworkers_events.id_coworker = ?);";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, coworkerId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int    id         = rs.getInt(1);
                String title      = rs.getString(2);
                String eventDescr = rs.getString(3);
                Date   date       = rs.getDate(4);
                String speaker    = rs.getString(5);
                int    spaceId    = rs.getInt(6);
                String qr         = rs.getString(7);

                CoworkingSpace space = getCoworkingSpace(spaceId);
                EventData eventData = new EventData(id, title, eventDescr, date, speaker, space);

                events.add(new CoworkerEvent(eventData, qr));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public static List<MetroSation> getMetroStations() {
        List<MetroSation> stations = new ArrayList<>();
        try {
            String query = "SELECT * FROM metro_stations;";

            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int    id       = rs.getInt(1);
                String title    = rs.getString(2);
                String locality = rs.getString(3);

                Coordinates coords = Coordinates.parse(locality);

                stations.add(new MetroSation(id, title, coords));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stations;
    }

    public static List<String> getMaritalStatuses() {
        return getTitles("marital_statuses");
    }

    public static List<String> getTools() {
        return getTitles("tools");
    }

    public static List<String> getPurposes() {
        return getTitles("purposes");
    }

    private static List<String> getTitles(String tableName) {
        List<String> titles = new ArrayList<>();
        try {
            String query = "SELECT title FROM " + tableName + ";";

            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String title = rs.getString(1);
                titles.add(title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return titles;
    }

    public static String getCoworkingQR(int idCoworkingSpace, int idCoworker) {
        String coworkingQR = null;
        try {
            String query = "SELECT qr FROM coworkers_spaces WHERE id_space = ? AND id_coworker = ?;";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, idCoworkingSpace);
            stmt.setInt(2, idCoworker);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                coworkingQR = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coworkingQR;
    }

    public static String getEventQR(int idEvent, int idCoworker) {
        String eventQR = null;
        try {
            String query = "SELECT qr FROM coworkers_events WHERE id_event = ? AND id_coworker = ?;";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, idEvent);
            stmt.setInt(2, idCoworker);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                eventQR = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventQR;
    }

    public static int getPurposeId(String purposeName) {
        return getId("purposes", purposeName);
    }

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        java.util.Date newDate = cal.getTime();
        return new java.sql.Date(newDate.getTime());
    }

    private static int getMaritalStatusId(String maritalStatusName) {
        return getId("marital_statuses", maritalStatusName);
    }

    private static int getToolId(String toolName) {
        return getId("tools", toolName);
    }

    private static int getId(String tableName, String title) {
        int id = -1;
        try {
            String query = "SELECT id FROM " + tableName + " WHERE title = ?;";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, title);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { id = rs.getInt(1); }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    private static int clearTools(int idCoworker, int idSpace) {
        int res = -1;
        try {
            String query = "DELETE FROM session_tools \n" +
                    "WHERE (id_coworker = ?) AND (id_space = ?);";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, idCoworker);
            stmt.setInt(2, idSpace);

            stmt.executeUpdate();
            res = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private static int clearTools(int idCoworker, int idSpace, String[] tools) {
        int res = -1;
        try {
            String query = "DELETE FROM session_tools \n" +
                           "WHERE (id_tool = ?) AND (id_coworker = ?) AND (id_space = ?);";
            PreparedStatement stmt = conn.prepareStatement(query);

            for (String tool: tools) {
                int idTool = getToolId(tool);

                if (idTool == -1) {
                    return -1;
                }

                stmt.setInt(1, idTool);
                stmt.setInt(2, idCoworker);
                stmt.setInt(3, idSpace);

                stmt.executeUpdate();
            }
            res = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
