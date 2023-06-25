package domain.System;

import java.time.LocalDateTime;

public class Event {
    private LocalDateTime eventDate;
    private String action;

    public Event(LocalDateTime eventDate, String action) {
        this.eventDate = eventDate;
        this.action = action;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventDate=" + eventDate +
                ", action='" + action + '\'' +
                '}';
    }
}
