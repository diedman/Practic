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

    public static boolean registerCoworker(String firstname,
                                        String lastName,
                                        String patronymic,
                                        String password,
                                        Date birthday,
                                        String email,
                                        String phoneNum,
                                        int idMaritalStatus) {
        try {
            String query = "INSERT INTO coworkers (firstname, lastname, patronymic, hashed_password, salt, birthday, email, phone_num, id_marital_status) \n" +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, firstname);
            stmt.setString(2, lastName);
            stmt.setString(3, patronymic);
            stmt.setString(4, password);
            stmt.setDate(  4, birthday);
            stmt.setString(5, email);
            stmt.setString(6, phoneNum);
            stmt.setInt(   7, idMaritalStatus);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean registerToCoworking(int idSpace, int idCoworker, String qr) {
        try {
            String query = "INSERT INTO coworkers_spaces (id_space, id_coworker, qr) \n" +
                    "VALUES (?, ?, ?);";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSpace);
            stmt.setInt(2, idCoworker);
            stmt.setString(3, qr);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
}
