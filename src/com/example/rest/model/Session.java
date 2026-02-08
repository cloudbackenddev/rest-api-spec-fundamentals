package com.example.rest.model;

public class Session {
    private String id;
    private String title;
    private String speaker;
    private String time;
    private int duration;

    public Session() {
    }

    public Session(String id, String title, String speaker, String time, int duration) {
        this.id = id;
        this.title = title;
        this.speaker = speaker;
        this.time = time;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", speaker='" + speaker + '\'' +
                ", time='" + time + '\'' +
                ", duration=" + duration +
                '}';
    }

    // simplistic JSON generation to avoid dependencies
    public String toJson() {
        return String.format("{\"id\":\"%s\", \"title\":\"%s\", \"speaker\":\"%s\", \"time\":\"%s\", \"duration\":%d}",
                id, title, speaker, time, duration);
    }
}
