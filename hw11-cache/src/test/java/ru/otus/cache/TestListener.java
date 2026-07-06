package ru.otus.cache;

import java.util.ArrayList;
import java.util.List;

public class TestListener implements HwListener<String, String> {

    private final List<String> events = new ArrayList<>();

    @Override
    public void notify(String key, String value, String action) {
        events.add(action);
    }

    public List<String> getEvents() {
        return events;
    }

    public void clear() {
        events.clear();
    }
}
