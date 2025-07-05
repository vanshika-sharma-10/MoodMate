package app.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:moodmate.db";

    public static void initializeDatabase() {

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS moods (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "date TEXT NOT NULL," +
                        "mood TEXT NOT NULL," +
                        "notes TEXT" +
                        "); ";
                stmt.execute(sql);
                System.out.println("✅ MoodMate DB Initialized");
            }
        }catch (SQLException e) {
                System.out.println("❌ DB Init Error: " + e.getMessage());
            }
        }

        public static void insertMood(String date, String mood, String notes) {

            String sql = "INSERT INTO moods(date, mood, notes) VALUES(?, ?, ?)";
            try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, date);
                pstmt.setString(2, mood);
                pstmt.setString(3, notes);
                pstmt.executeUpdate();
                System.out.println("✅ Mood saved to DB!");
            } catch (SQLException e) {
                System.out.println("❌ DB Insert Error: " + e.getMessage());
            }

    }
    public static List<MoodEntry> getAllMoods() {
        List<MoodEntry> moods = new ArrayList<>();
        String sql = "SELECT date, mood, notes FROM moods ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String date = rs.getString("date");
                String mood = rs.getString("mood");
                String notes = rs.getString("notes");
                moods.add(new MoodEntry(date, mood, notes));
            }

        } catch (SQLException e) {
            System.out.println("❌ DB Fetch Error: " + e.getMessage());
        }

        return moods;
    }
    public static void deleteMood(String date, String mood, String notes) {
        String sql = "DELETE FROM moods WHERE date = ? AND mood = ? AND notes = ? LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, date);
            pstmt.setString(2, mood);
            pstmt.setString(3, notes);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ DB Deletion Error: " + e.getMessage());
        }
    }
}
