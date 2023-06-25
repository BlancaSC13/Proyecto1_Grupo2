package domain.System;

import java.util.ArrayList;
import java.util.List;

public class Logbooks {
    private String user;
    private List<Event> events = new ArrayList<>();

    public Logbooks() {
    }

    public Logbooks(String user, Event events) {
        this.user = user;
        this.events.add(events);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "Logbooks{" +
                "user='" + user + '\'' +
                ", events=" + events +
                '}';
    }
}
