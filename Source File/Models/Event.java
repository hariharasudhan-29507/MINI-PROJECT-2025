package models;

    import java.sql.Date;
    import java.time.LocalDate;

    public class Event {
        private int eventId;
        private String eventName;
        private Date eventDate;
        private String venue;
        private String city;
        private String country;
        private String status;  // 'upcoming', 'completed', 'cancelled'
        private int createdBy;

        // Constructors
        public Event() {}

        public Event(String eventName, Date eventDate, String venue, String city, String country) {
            this.eventName = eventName;
        this.eventDate = eventDate;
        this.venue = venue;
        this.city = city;
        this.country = country;
        this.status = "upcoming";
    }

    // Getters and Setters
    public int getEventId() { 
        return eventId; 
    }
    
    public void setEventId(int eventId) { 
        this.eventId = eventId; 
    }

    public String getEventName() { 
        return eventName; 
    }
    
    public void setEventName(String eventName) { 
        this.eventName = eventName; 
    }

    public Date getEventDate() { 
        return eventDate; 
    }
    
    public void setEventDate(Date eventDate) { 
        this.eventDate = eventDate; 
    }

    public String getVenue() { 
        return venue; 
    }
    
    public void setVenue(String venue) { 
        this.venue = venue; 
    }

    public String getCity() { 
        return city; 
    }
    
    public void setCity(String city) { 
        this.city = city; 
    }

    public String getCountry() { 
        return country; 
    }
    
    public void setCountry(String country) { 
        this.country = country; 
    }

    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }

    public int getCreatedBy() { 
        return createdBy; 
    }
    
    public void setCreatedBy(int createdBy) { 
        this.createdBy = createdBy; 
    }

    // Utility Methods
    public boolean isUpcoming() {
        return "upcoming".equalsIgnoreCase(status);
    }

    public boolean isCompleted() {
        return "completed".equalsIgnoreCase(status);
    }

    public boolean isCancelled() {
        return "cancelled".equalsIgnoreCase(status);
    }

    public String getLocation() {
        return venue + ", " + city + ", " + country;
    }

    @Override
    public String toString() {
        return eventName + " - " + city + ", " + country + " (" + eventDate + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return eventId == event.eventId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(eventId);
    }
}