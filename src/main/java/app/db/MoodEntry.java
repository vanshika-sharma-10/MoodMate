package app.db;

public class MoodEntry {
    private String date;
    private String mood;
    private String notes;

    public MoodEntry(String date, String mood, String notes) {
        this.date = date;
        this.mood = mood;
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public String getMood() {
        return mood;
    }

    public String getNotes() {
        return notes;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}