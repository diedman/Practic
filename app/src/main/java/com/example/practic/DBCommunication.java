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

            String query = "INSERT INTO coworkers (firstname, lastname, patronymic, hashed_password, salt, birthday, email, phone_num, id_marital_status) \n" +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

            String hashedPassword = PasswordAuthentication.getHashedPassword(password);

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, firstname);
            stmt.setString(2, lastName);
            stmt.setString(3, patronymic);
            stmt.setString(4, hashedPassword);
            stmt.setDate(4, birthday);
            stmt.setString(5, email);
            stmt.setString(6, phoneNum);
            stmt.setInt(7, idMaritalStatus);

            stmt.executeUpdate();
            res = 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int registerToCoworking(int idSpace, int idCoworker, String qr) {
        int res = -1;
        try {
            String query = "INSERT INTO coworkers_spaces (id_space, id_coworker, qr) \n" +
                    "VALUES (?, ?, ?);";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSpace);
            stmt.setInt(2, idCoworker);
            stmt.setString(3, qr);

            stmt.executeUpdate();
            res = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static boolean registerToEvent(int idEvent, int idCoworker, String qr) {
        try {
            String query = "INSERT INTO coworkers_events (id_event, id_coworker, qr) \n" +
                    "VALUES (?, ?, ?);";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, idEvent);
            stmt.setInt(2, idCoworker);
            stmt.setString(3, qr);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static int authenticateUser(String email, String password) {
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

    public static List<CoworkingSpace> getCoworkingSpaces() {
        List<CoworkingSpace> spaces = new ArrayList<>();
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

                spaces.add(new CoworkingSpace(id, title, coords[0], coords[1]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spaces;
    }

    public static List<CoworkingSpace> getSpacesOfCoworker(int coworkerId) {
        List<CoworkingSpace> spaces = new ArrayList<>();
        try {
            String query = "SELECT * FROM spaces WHERE id IN (\n" +
                    "SELECT id_space FROM coworkers_spaces WHERE id_coworker = ?\n" +
                    ");";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, coworkerId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String title = rs.getString(2);
                String locality = rs.getString(3);

                double[] coords = Arrays.stream(locality.split(":"))
                        .mapToDouble(Double::parseDouble)
                        .toArray();

                spaces.add(new CoworkingSpace(id, title, coords[0], coords[1]));
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

    private static int getMaritalStatusId(String maritalStatus) {
        int id = -1;
        try {
            String query = "SELECT id FROM marital_statuses WHERE title = ?;";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, maritalStatus);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { id = rs.getInt(1); }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
