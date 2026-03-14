package dao;

import utils.DatabaseConnection;
import models.Event;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    private Connection connection;

    public EventDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Event> getUpcomingEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events WHERE status = 'upcoming' ORDER BY event_date";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Event event = extractEventFromResultSet(rs);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return events;
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events ORDER BY event_date DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Event event = extractEventFromResultSet(rs);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return events;
    }

    public Event getEventById(int eventId) {
        String query = "SELECT * FROM events WHERE event_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractEventFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public boolean insertEvent(Event event) {
        String query = "INSERT INTO events (event_id, event_name, event_date, venue, city, country, status, created_by) " +
                      "VALUES (events_seq.NEXTVAL, ?, ?, ?, ?, ?, 'upcoming', ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, event.getEventName());
            pstmt.setDate(2, event.getEventDate());
            pstmt.setString(3, event.getVenue());
            pstmt.setString(4, event.getCity());
            pstmt.setString(5, event.getCountry());
            pstmt.setInt(6, event.getCreatedBy());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateEvent(Event event) {
        String query = "UPDATE events SET event_name = ?, event_date = ?, venue = ?, " +
                      "city = ?, country = ?, status = ? WHERE event_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, event.getEventName());
            pstmt.setDate(2, event.getEventDate());
            pstmt.setString(3, event.getVenue());
            pstmt.setString(4, event.getCity());
            pstmt.setString(5, event.getCountry());
            pstmt.setString(6, event.getStatus());
            pstmt.setInt(7, event.getEventId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateEventStatus(int eventId, String status) {
        String query = "UPDATE events SET status = ? WHERE event_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, eventId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Event extractEventFromResultSet(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getInt("event_id"));
        event.setEventName(rs.getString("event_name"));
        event.setEventDate(rs.getDate("event_date"));
        event.setVenue(rs.getString("venue"));
        event.setCity(rs.getString("city"));
        event.setCountry(rs.getString("country"));
        event.setStatus(rs.getString("status"));
        event.setCreatedBy(rs.getInt("created_by"));
        return event;
    }
}