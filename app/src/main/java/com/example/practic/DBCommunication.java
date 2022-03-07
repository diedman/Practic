package com.example.practic;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBCommunication {
    private static Connection conn = DBUtil.getConnection();

    public static int ifCoworkerExists(String email) {
        int res = 0;
        try {
            String query = "SELECT * FROM coworkers WHERE email = ?;";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { res = 1; } else { res = -1; }
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
                                       String maritalStatus) {
        int res = -1;

        try {
            if (ifCoworkerExists(email) == 1) { return 0; }

            int idMaritalStatus = getMaritalStatusId(maritalStatus);

            if (idMaritalStatus == -1) {
                return -1;
            }

            String query = "INSERT INTO coworkers (firstname, lastname, patronymic, hashed_password, birthday, email, phone_num, id_marital_status) \n" +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

            String hashedPassword = PasswordAuthentication.getHashedPassword(password);

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, firstname);
            stmt.setString(2, lastName);
            stmt.setString(3, patronymic);
            stmt.setString(4, hashedPassword);
            stmt.setDate(5, birthday);
            stmt.setString(6, email);
            stmt.setString(7, phoneNum);
            stmt.setInt(8, idMaritalStatus);

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
                                          Date startSession,
                                          Date endSession,
                                          String purposeName,
                                          String[] tools) {
        int res = -1;

        try {
            String query = "INSERT INTO coworkers_spaces (id_space, id_coworker, qr, " +
                            "start_session, end_session, id_purpose) \n" +
                           "VALUES (?, ?, ?, ?, ?, ?);";

            int IdPurpose = getPurposeId(purposeName);

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSpace);
            stmt.setInt(2, idCoworker);
            stmt.setString(3, qr);
            stmt.setDate(4, startSession);
            stmt.setDate(5, endSession);
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
        try {
            String query = "INSERT INTO session_tools (id_tool, id_coworker, id_space) \n" +
                    "VALUES (?, ?, ?)";
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

    public static int registerOnEvent(int idEvent, int idCoworker, String qr) {
        int res = -1;
        try {
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
                           "coworkers.email, coworkers.phone_num, marital_statuses.title \n" +
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
                CoworkerData.maritalStatus  = rs.getString(9);

                res = PasswordAuthentication.authenticate(password, hashedPassword) ? 1 : 0;
            } else {
                res = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static List<CoworkerSpace> getCoworkingSpaces() {
        List<CoworkerSpace> spaces = new ArrayList<>();
        try {
            String query = "SELECT * FROM spaces;";

            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String title = rs.getString(2);
                String locality = rs.getString(3);

                double[] coords = Arrays.stream(locality.split(":"))
                        .mapToDouble(Double::parseDouble)
                        .toArray();

                spaces.add(new CoworkerSpace(id, title, coords[0], coords[1]));
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
                    "spaces.locality \n" +
                    "FROM coworkers_spaces, spaces, purposes \n" +
                    "WHERE (spaces.id = coworkers_spaces.id_space) " +
                    "AND (purposes.id = coworkers_spaces.id_purpose);";

            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idSpace       = rs.getInt(1);
                String spaceName  = rs.getString(2);
                String qr         = rs.getString(3);
                String purpose    = rs.getString(4);
                Date startSession = rs.getDate(5);
                Date endSession   = rs.getDate(6);
                String locality   = rs.getString(7);

                Coordinates coords = Coordinates.parse(locality);

                CoworkerSpace space = new CoworkerSpace(idSpace, spaceName, coords);

                spaces.add(new CoworkingSession(space, qr, purpose, startSession, endSession));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spaces;
    }

    public static List<Event> getCoworkingEvents(int idCoworkingSpace) {
        List<Event> events = new ArrayList<>();
        try {
            String query = "SELECT id, title, theme, meeting_date FROM events WHERE id_space = ?;";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, idCoworkingSpace);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int    id    = rs.getInt(1);
                String title = rs.getString(2);
                String theme = rs.getString(3);
                Date   date  = rs.getDate(4);

                events.add(new Event(id, title, theme, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public static List<Event> getEventsOfCoworker(int coworkerId) {
        List<Event> events = new ArrayList<>();
        try {
            String query = "SELECT id, title, theme, meeting_date FROM events WHERE id IN (\n" +
                    "SELECT id_event FROM coworkers_events WHERE id_coworker = ?" +
                    "\n);";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, coworkerId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int    id    = rs.getInt(1);
                String title = rs.getString(2);
                String theme = rs.getString(3);
                Date   date  = rs.getDate(4);

                events.add(new Event(id, title, theme, date));
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

    private static int getMaritalStatusId(String maritalStatusName) {
        return getId("marital_statuses", maritalStatusName);
    }

    private static int getToolId(String toolName) {
        return getId("tools", toolName);
    }

    private static int getPurposeId(String purposeName) {
        return getId("purposes", purposeName);
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
