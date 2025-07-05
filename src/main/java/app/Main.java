package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import app.db.DatabaseHelper;
import app.db.MoodEntry;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.chart.PieChart;
import java.time.LocalDate;
import javafx.geometry.Side;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Font.loadFont(Main.class.getResource("/assets/fonts/Satisfy-Regular.ttf").toExternalForm(), 32);

        showDashboard(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
    }

    private void showMoodEntryForm(Stage stage) {
        DatabaseHelper.initializeDatabase();

        Label title = new Label("MoodMate 💖");
        title.setStyle("""
    -fx-font-size: 40px;
    -fx-font-family: 'Satisfy', cursive;
    -fx-text-fill: #BA68C8;
    -fx-effect: dropshadow(gaussian, #F8BBD0, 4, 0.3, 1, 1); """);
        title.setPadding(new Insets(10, 0, 20, 0));


        Label dateLabel = new Label("Select Date:");
        dateLabel.setStyle("""
        -fx-font-size: 16px;
        -fx-font-family: 'Comic Sans MS', cursive;
        -fx-text-fill: #6A1B9A;
        -fx-effect: dropshadow(gaussian, rgba(255,192,203,0.5), 2, 0.2, 1, 1); """);

        DatePicker datePicker = new DatePicker();
        datePicker.setStyle("""
    -fx-pref-width: 250px;
    -fx-font-size: 13px;
    -fx-background-radius: 10;
    -fx-border-radius: 10;
""");

        Label moodLabel = new Label("Select Mood:");
        moodLabel.setStyle("""
        -fx-font-size: 16px;
        -fx-font-family: 'Comic Sans MS', cursive;
        -fx-text-fill: #6A1B9A;
        -fx-effect: dropshadow(gaussian, rgba(255,192,203,0.5), 2, 0.2, 1, 1); """);

        ComboBox<String> moodCombo = new ComboBox<>();
        moodCombo.getItems().addAll("Happy 😊", "Sad 😔", "Angry 😠", "Calm 😌", "Stressed 😩", "Excited ✨");
        moodCombo.setPromptText("Choose your mood");
        moodCombo.setStyle("""
    -fx-pref-width: 250px;
    -fx-font-size: 13px;
    -fx-background-radius: 10;
    -fx-border-radius: 10;
""");

        Label notesLabel = new Label("Write Notes:");
        notesLabel.setStyle("""
        -fx-font-size: 16px;
        -fx-font-family: 'Comic Sans MS', cursive;
        -fx-text-fill: #6A1B9A;
        -fx-effect: dropshadow(gaussian, rgba(255,192,203,0.5), 2, 0.2, 1, 1); """);

        TextArea notesArea = new TextArea();
        notesArea.setPromptText("How are you feeling today?");
        notesArea.setPrefRowCount(4);
        notesArea.setStyle("""
    -fx-background-radius: 10;
    -fx-border-radius: 10;
    -fx-font-size: 13px;
    -fx-effect: dropshadow(gaussian, lightgray, 5, 0.3, 2, 2);
""");

        Button saveButton = new Button("Save Mood");
        saveButton.setStyle("""
    -fx-background-color: #CE93D8;
    -fx-text-fill: white;
    -fx-font-weight: bold;
    -fx-background-radius: 12;
    -fx-font-size: 14px;
    -fx-cursor: hand;
    -fx-effect: dropshadow(gaussian, #B39DDB, 3, 0.3, 1, 1);
""");

        Button viewHistoryButton = new Button("View Mood History");
        viewHistoryButton.setStyle("""
    -fx-background-color: #B2DFDB;
    -fx-text-fill: #004D40;
    -fx-font-weight: bold;
    -fx-background-radius: 12;
    -fx-font-size: 14px;
    -fx-cursor: hand;
    -fx-effect: dropshadow(gaussian, #80CBC4, 2, 0.2, 1, 1);
""");
        viewHistoryButton.setOnAction(e -> showMoodHistory());

        saveButton.setOnAction(e -> {
            String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
            datePicker.setStyle("-fx-pref-width: 250px; -fx-font-size: 13px;");

            String mood = moodCombo.getValue();
            moodCombo.setStyle("-fx-pref-width: 250px; -fx-font-size: 13px;");

            String notes = notesArea.getText();
            notesArea.setStyle("""
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-font-size: 13px;
            -fx-effect: dropshadow(gaussian, lightgray, 5, 0.3, 2, 2);
        """);

            if (!date.isEmpty() && mood != null && !notes.isEmpty()) {
                DatabaseHelper.insertMood(date, mood, notes);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Mood Saved");
                alert.setHeaderText(null);
                alert.setContentText("Your mood has been saved successfully 💚");
                alert.showAndWait();

                datePicker.setValue(null);
                moodCombo.setValue(null);
                notesArea.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Incomplete Details");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the details before saving❗");
                alert.showAndWait();
            }
        });

        Button backButton = new Button("← Back to Dashboard");
        backButton.setStyle("""
    -fx-background-color: #F8BBD0;
    -fx-text-fill: #880E4F;
    -fx-border-color: #880E4F;
    -fx-border-width: 2;
    -fx-border-radius: 10;
    -fx-background-radius: 10;
    -fx-font-size: 13px;
    -fx-padding: 8 18;
    -fx-effect: dropshadow(gaussian, #F48FB1, 2, 0.2, 1, 1); """);

        backButton.setOnMouseEntered(e -> backButton.setScaleX(0.95));
        backButton.setOnMouseEntered(e -> backButton.setScaleY(0.95));
        backButton.setOnMouseExited(e -> backButton.setScaleX(1.0));
        backButton.setOnMouseExited(e -> backButton.setScaleY(1.0));

        backButton.setOnAction(e -> {
            stage.setWidth(500);
            stage.setHeight(500);
            showDashboard(stage);
        });
        showDashboard(stage);

        VBox root = new VBox(12, title, dateLabel, datePicker, moodLabel, moodCombo, notesLabel, notesArea, saveButton, viewHistoryButton, backButton);
        root.setStyle("""
    -fx-background-image: url('/assets/log-bg.jpeg'); 
    -fx-background-size: cover;
    -fx-background-position: center;
""");
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 570, 650);
        stage.setScene(scene);
        stage.setWidth(570);
        stage.setHeight(600);
    }
    private void showDashboard(Stage primaryStage) {
        primaryStage.setTitle("MoodMate - Dashboard");
        Label outlineLabel = new Label("Welcome to MoodMate");
        outlineLabel.setStyle("""
    -fx-font-size: 46px;
    -fx-text-fill: #6A1B9A; /* Outline Color */
    -fx-font-family: 'Satisfy';
""");

        Label welcome = new Label("Welcome to MoodMate");
        welcome.setStyle("""
    -fx-font-size: 46px;
    -fx-text-fill: white; /* Fill Color */
    -fx-font-family: 'Satisfy';
    -fx-effect: dropshadow(gaussian, #BA68C8, 3, 0.5, 1, 1);
""");

        StackPane stackedHeading = new StackPane(outlineLabel, welcome);
        StackPane.setAlignment(welcome, Pos.CENTER);
        StackPane.setAlignment(outlineLabel, Pos.CENTER);

        Button logMoodButton = new Button("➕ Log Mood");
        logMoodButton.setStyle("""
    -fx-background-color: white;
    -fx-text-fill: #4A148C;  /* Deep Purple Text */
    -fx-border-color: #4A148C;
    -fx-border-width: 2;
    -fx-background-radius: 10;
    -fx-border-radius: 10;
    -fx-font-size: 14px;
    -fx-padding: 10 22;
""");

        logMoodButton.setOnMouseEntered(e -> {
            logMoodButton.setStyle("""
        -fx-background-color: #EDE7F6;
        -fx-text-fill: #4A148C;
        -fx-border-color: #4A148C;
        -fx-border-width: 2;
        -fx-background-radius: 10;
        -fx-border-radius: 10;
        -fx-font-size: 14px;
        -fx-padding: 10 22;
    """);
            logMoodButton.setScaleX(0.95);
            logMoodButton.setScaleY(0.95);
        });

        logMoodButton.setOnMouseExited(e -> {
            logMoodButton.setStyle("""
        -fx-background-color: white;
        -fx-text-fill: #4A148C;
        -fx-border-color: #4A148C;
        -fx-border-width: 2;
        -fx-background-radius: 10;
        -fx-border-radius: 10;
        -fx-font-size: 14px;
        -fx-padding: 10 22;
    """);
            logMoodButton.setScaleX(1.0);
            logMoodButton.setScaleY(1.0);
        });

        Button viewHistoryButton = new Button("📖 View Mood History");
        viewHistoryButton.setStyle("""
    -fx-background-color: white;
    -fx-text-fill: #4A148C;  /* Deep Purple Text */
    -fx-border-color: #4A148C;
    -fx-border-width: 2;
    -fx-background-radius: 10;
    -fx-border-radius: 10;
    -fx-font-size: 14px;
    -fx-padding: 10 22;
""");

        viewHistoryButton.setOnMouseEntered(e -> {
            viewHistoryButton.setStyle("""
        -fx-background-color: #EDE7F6;
        -fx-text-fill: #4A148C;
        -fx-border-color: #4A148C;
        -fx-border-width: 2;
        -fx-background-radius: 10;
        -fx-border-radius: 10;
        -fx-font-size: 14px;
        -fx-padding: 10 22;
    """);
            viewHistoryButton.setScaleX(0.95);
            viewHistoryButton.setScaleY(0.95);
        });

        viewHistoryButton.setOnMouseExited(e -> {
            viewHistoryButton.setStyle("""
        -fx-background-color: white;
        -fx-text-fill: #4A148C;
        -fx-border-color: #4A148C;
        -fx-border-width: 2;
        -fx-background-radius: 10;
        -fx-border-radius: 10;
        -fx-font-size: 14px;
        -fx-padding: 10 22;
    """);
            viewHistoryButton.setScaleX(1.0);
            viewHistoryButton.setScaleY(1.0);
        });

        logMoodButton.setOnAction(e -> showMoodEntryForm(primaryStage));

        viewHistoryButton.setOnAction(e -> showMoodHistory());

        Button viewStatsButton = new Button("📊 View Mood Stats");
        viewStatsButton.setStyle("""
    -fx-background-color: white;
    -fx-text-fill: #4A148C;  /* Deep Purple Text */
    -fx-border-color: #4A148C;
    -fx-border-width: 2;
    -fx-background-radius: 10;
    -fx-border-radius: 10;
    -fx-font-size: 14px;
    -fx-padding: 10 22;
""");

        viewStatsButton.setOnMouseEntered(e -> {
            viewStatsButton.setStyle("""
        -fx-background-color: #EDE7F6;
        -fx-text-fill: #4A148C;
        -fx-border-color: #4A148C;
        -fx-border-width: 2;
        -fx-background-radius: 10;
        -fx-border-radius: 10;
        -fx-font-size: 14px;
        -fx-padding: 10 22;
    """);
            viewStatsButton.setScaleX(0.95);
            viewStatsButton.setScaleY(0.95);
        });

        viewStatsButton.setOnMouseExited(e -> {
            viewStatsButton.setStyle("""
        -fx-background-color: white;
        -fx-text-fill: #4A148C;
        -fx-border-color: #4A148C;
        -fx-border-width: 2;
        -fx-background-radius: 10;
        -fx-border-radius: 10;
        -fx-font-size: 14px;
        -fx-padding: 10 22;
    """);
            viewStatsButton.setScaleX(1.0);
            viewStatsButton.setScaleY(1.0);
        });

        viewStatsButton.setOnAction(e -> showMoodStatistics());

        Button viewCalendarButton = new Button("📅 Mood Calendar");
        viewCalendarButton.setStyle("""
    -fx-background-color: white;
    -fx-text-fill: #4A148C;  /* Deep Purple Text */
    -fx-border-color: #4A148C;
    -fx-border-width: 2;
    -fx-background-radius: 10;
    -fx-border-radius: 10;
    -fx-font-size: 14px;
    -fx-padding: 10 22;
""");

        viewCalendarButton.setOnMouseEntered(e -> {
            viewCalendarButton.setStyle("""
        -fx-background-color: #EDE7F6;
        -fx-text-fill: #4A148C;
        -fx-border-color: #4A148C;
        -fx-border-width: 2;
        -fx-background-radius: 10;
        -fx-border-radius: 10;
        -fx-font-size: 14px;
        -fx-padding: 10 22;
    """);
            viewCalendarButton.setScaleX(0.95);
            viewCalendarButton.setScaleY(0.95);
        });

        viewCalendarButton.setOnMouseExited(e -> {
            viewCalendarButton.setStyle("""
        -fx-background-color: white;
        -fx-text-fill: #4A148C;
        -fx-border-color: #4A148C;
        -fx-border-width: 2;
        -fx-background-radius: 10;
        -fx-border-radius: 10;
        -fx-font-size: 14px;
        -fx-padding: 10 22;
    """);
            viewCalendarButton.setScaleX(1.0);
            viewCalendarButton.setScaleY(1.0);
        });

        viewCalendarButton.setOnAction(e -> showMoodCalendarView());

        VBox layout = new VBox(20, stackedHeading, logMoodButton, viewHistoryButton, viewStatsButton, viewCalendarButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-image: url('/assets/smiles-bg.jpeg'); -fx-background-size: cover; -fx-background-position: center center;");

        Scene dashboardScene = new Scene(layout, 500, 400);
        primaryStage.setScene(dashboardScene);
        primaryStage.show();
    }
    public static void main(String args[]){
        launch(args);
    }

    private void showMoodHistory() {

        Stage tableStage = new Stage();
        tableStage.setTitle("MoodMate - Mood History");

        Label header = new Label("🌈 Mood Timeline");
        header.setStyle("""
    -fx-font-size: 26px;
    -fx-font-family: 'Satisfy';
    -fx-text-fill: #4A148C;
    -fx-effect: dropshadow(gaussian, #CE93D8, 3, 0.3, 1, 1);
""");

        TableView<MoodEntry> table = new TableView<>();
        table.setStyle("""
    -fx-font-size: 14px;
    -fx-font-family: 'Comic Sans MS', cursive;
    -fx-background-color: white;
    -fx-border-radius: 10;
    -fx-background-radius: 10;
    -fx-effect: dropshadow(gaussian, #D1C4E9, 4, 0.3, 2, 2);
""");

        TableColumn<MoodEntry, String> dateCol = new TableColumn<>("Date");
        dateCol.setStyle("-fx-alignment: CENTER;");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate()));

        TableColumn<MoodEntry, String> moodCol = new TableColumn<>("Mood");
        moodCol.setStyle("-fx-alignment: CENTER;");
        moodCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMood()));

        TableColumn<MoodEntry, String> notesCol = new TableColumn<>("Notes");
        notesCol.setStyle("-fx-alignment: CENTER_LEFT;");
        notesCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNotes()));

        table.getColumns().addAll(dateCol, moodCol, notesCol);

        table.setRowFactory(tv -> {
            TableRow<MoodEntry> row = new TableRow<>();
            row.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
                if (isNowHovered && !row.isEmpty()) {
                    row.setStyle("-fx-background-color: #F3E5F5;");
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });

        Label filterLabel = new Label("Filter by Mood:");
        filterLabel.setStyle("""
    -fx-font-size: 16px;
    -fx-font-family: 'Comic Sans MS', cursive;
    -fx-text-fill: #4A148C;
    -fx-effect: dropshadow(gaussian, rgba(255,192,203,0.5), 2, 0.2, 1, 1);
""");
        ComboBox<String> moodFilter = new ComboBox<>();
        moodFilter.setStyle("""
    -fx-background-radius: 10;
    -fx-border-radius: 10;
    -fx-font-size: 13px;
""");
        moodFilter.getItems().addAll("All", "Happy 😊", "Sad 😔", "Angry 😠", "Calm 😌", "Stressed 😩", "Excited ✨");
        moodFilter.setValue("All");

        ObservableList<MoodEntry> fullMoodList = FXCollections.observableArrayList(DatabaseHelper.getAllMoods());
        table.setItems(fullMoodList);

        moodFilter.setOnAction(e -> {
            String selectedMood = moodFilter.getValue();
            if (selectedMood.equals("All")) {
                table.setItems(fullMoodList);
            } else {
                ObservableList<MoodEntry> filtered = FXCollections.observableArrayList();
                for (MoodEntry entry : fullMoodList) {
                    if (entry.getMood().equals(selectedMood)) {
                        filtered.add(entry);
                    }
                }
                table.setItems(filtered);
            }
        });
        Button deleteButton = new Button("🗑️ Delete Entry");
        deleteButton.setStyle("""
    -fx-background-color: #E57373;
    -fx-text-fill: white;
    -fx-background-radius: 10;
    -fx-font-weight: bold;
    -fx-effect: dropshadow(gaussian, #EF9A9A, 3, 0.3, 2, 2);
""");
        deleteButton.setOnAction(e -> {
            MoodEntry selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                DatabaseHelper.deleteMood(selected.getDate(), selected.getMood(), selected.getNotes());
                table.getItems().remove(selected);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Mood Deleted");
                alert.setHeaderText(null);
                alert.setContentText("Mood entry deleted successfully.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Selection");
                alert.setHeaderText(null);
                alert.setContentText("Please select a mood entry to delete.");
                alert.showAndWait();
            }
        });

        Button backButton = new Button("⬅ Back to Dashboard");
        backButton.setStyle("""
    -fx-background-color: #F8BBD0;
    -fx-text-fill: #880E4F;
    -fx-border-color: #880E4F;
    -fx-border-width: 2;
    -fx-border-radius: 10;
    -fx-background-radius: 10;
    -fx-font-size: 13px;
    -fx-padding: 8 18;
    -fx-effect: dropshadow(gaussian, #F48FB1, 2, 0.2, 1, 1);
""");
        backButton.setOnAction(e -> {
            tableStage.close();
        });

        VBox vbox = new VBox(15, header, filterLabel, moodFilter, table, deleteButton, backButton);
        vbox.setStyle("""
    -fx-background-color: linear-gradient(to bottom right, #F3E5F5, #E1BEE7, #F8BBD0);
    -fx-padding: 30;
""");

        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 570, 600);
        tableStage.setScene(scene);
        tableStage.setWidth(570);
        tableStage.setHeight(600);
        tableStage.show();
    }
    private void showMoodStatistics() {
        Stage statsStage = new Stage();
        statsStage.setTitle("MoodMate - Mood Statistics");

        Label title = new Label("📊 Mood Reflections");
        title.setStyle("""
        -fx-font-size: 28px;
        -fx-font-family: 'Satisfy';
        -fx-text-fill: #6A1B9A;
        -fx-effect: dropshadow(gaussian, #E1BEE7, 4, 0.6, 2, 2);
    """);

        List<MoodEntry> allMoods = DatabaseHelper.getAllMoods();
        int totalEntries = allMoods.size();

        Map<String, Integer> moodFrequency = new HashMap<>();
        for (MoodEntry entry : allMoods) {
            String mood = entry.getMood();

            if (mood.contains("🤩")) {
                mood = mood.replace("🤩", "✨");
            }

            moodFrequency.put(mood, moodFrequency.getOrDefault(mood, 0) + 1);
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : moodFrequency.entrySet()) {
            pieData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        PieChart moodChart = new PieChart(pieData);
        moodChart.setTitle("Mood Distribution");
        moodChart.setLabelsVisible(true);
        moodChart.setLegendVisible(true);
        moodChart.setLegendSide(Side.BOTTOM);
        moodChart.setPrefHeight(260);
        moodChart.setStyle("-fx-font-size: 14px;");

        VBox chartContainer = new VBox(moodChart);
        chartContainer.setAlignment(Pos.CENTER);
        chartContainer.setStyle("""
        -fx-background-color: rgba(255, 255, 255, 0.6);
        -fx-background-radius: 20;
        -fx-padding: 20;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0.3, 4, 4);
    """);

        String mostFrequentMood = "None";
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : moodFrequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequentMood = entry.getKey();
            }
        }

        Label totalLabel = new Label("📌 Total Mood Entries:\n" + totalEntries);
        Label frequentLabel = new Label("🌟 Most Frequent Mood:\n" + mostFrequentMood + " (" + maxCount + " times)");

        for (Label label : List.of(totalLabel, frequentLabel)) {
            label.setStyle("""
            -fx-font-size: 16px;
            -fx-background-color: #F8F8FF;
            -fx-padding: 15;
            -fx-background-radius: 16;
            -fx-effect: dropshadow(gaussian, #D1C4E9, 5, 0.4, 2, 2);
            -fx-text-fill: #4A148C;
        """);
            label.setMaxWidth(260);
            label.setAlignment(Pos.CENTER);
            label.setWrapText(true);
        }

        Button backButton = new Button("⬅ Back to Dashboard");
        backButton.setStyle("""
        -fx-background-color: #E1BEE7;
        -fx-text-fill: #6A1B9A;
        -fx-font-weight: bold;
        -fx-background-radius: 10;
        -fx-padding: 10 20;
        -fx-effect: dropshadow(gaussian, #CE93D8, 2, 0.3, 1, 1);
    """);

        backButton.setOnMouseEntered(e -> {
            backButton.setStyle("""
            -fx-background-color: #D1C4E9;
            -fx-text-fill: #4A148C;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-padding: 10 20;
            -fx-effect: dropshadow(gaussian, #BA68C8, 2, 0.3, 1, 1);
        """);
            backButton.setScaleX(0.95);
            backButton.setScaleY(0.95);
        });

        backButton.setOnMouseExited(e -> {
            backButton.setStyle("""
            -fx-background-color: #E1BEE7;
            -fx-text-fill: #6A1B9A;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-padding: 10 20;
            -fx-effect: dropshadow(gaussian, #CE93D8, 2, 0.3, 1, 1);
        """);
            backButton.setScaleX(1.0);
            backButton.setScaleY(1.0);
        });

        backButton.setOnAction(e -> statsStage.close());

        VBox layout = new VBox(20, title, chartContainer, totalLabel, frequentLabel, backButton);
        layout.setPadding(new Insets(20));layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("""
        -fx-background-color: linear-gradient(to bottom right, #E1BEE7, #F3E5F5, #BBDEFB);
    """);

        Scene scene = new Scene(layout, 540, 740);
        statsStage.setScene(scene);
        statsStage.setWidth(540);
        statsStage.setHeight(740);
        statsStage.setResizable(false);
        statsStage.show();
    }

    private void showMoodCalendarView() {
        Stage calendarStage = new Stage();
        calendarStage.setTitle("MoodMate - Mood Calendar");

        Label header = new Label("📅 Mood Calendar");
        header.setStyle("""
        -fx-font-size: 28px;
        -fx-font-family: 'Satisfy';
        -fx-text-fill: #6A1B9A;
        -fx-effect: dropshadow(gaussian, #E1BEE7, 4, 0.5, 2, 2);
    """);

        Label monthLabel = new Label();
        monthLabel.setStyle("""
        -fx-font-size: 18px;
        -fx-font-weight: bold;
        -fx-text-fill: #4A148C;
    """);

        Button prevMonth = new Button("◀");
        prevMonth.setStyle("-fx-font-size: 18px; -fx-text-fill: #6A1B9A;");

        Button nextMonth = new Button("▶");
        nextMonth.setStyle("-fx-font-size: 18px; -fx-text-fill: #6A1B9A;");

        HBox topControls = new HBox(10, prevMonth, monthLabel, nextMonth);
        topControls.setAlignment(Pos.CENTER);

        GridPane calendarGrid = new GridPane();
        calendarGrid.setHgap(10);
        calendarGrid.setVgap(10);
        calendarGrid.setPadding(new Insets(20));
        calendarGrid.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, header, topControls, calendarGrid);
        layout.setStyle("""
        -fx-background-color: linear-gradient(to bottom right, #F3E5F5, #E1BEE7, #BBDEFB);
        -fx-padding: 30;
    """);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 500);
        calendarStage.setScene(scene);
        calendarStage.setWidth(650);
        calendarStage.setHeight(600);
        calendarStage.setResizable(false);
        calendarStage.show();

        final LocalDate[] currentDate = { LocalDate.now() };

        Map<LocalDate, MoodEntry> moodMap = new HashMap<>();
        for (MoodEntry entry : DatabaseHelper.getAllMoods()) {
            moodMap.put(LocalDate.parse(entry.getDate()), entry);
        }

        Runnable updateCalendar = () -> {
            calendarGrid.getChildren().clear();

            // 🌸 Weekday headers
            String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
            for (int i = 0; i < days.length; i++) {
                Label dayLabel = new Label(days[i]);
                dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #4A148C; -fx-font-size: 14px;");
                calendarGrid.add(dayLabel, i, 0);  // Row 0
            }

            LocalDate firstOfMonth = currentDate[0].withDayOfMonth(1);
            int daysInMonth = currentDate[0].lengthOfMonth();
            int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1=Monday

            monthLabel.setText(currentDate[0].getMonth() + " " + currentDate[0].getYear());

            for (int day = 1; day <= daysInMonth; day++) {
                LocalDate date = currentDate[0].withDayOfMonth(day);
                MoodEntry entry = moodMap.get(date);
                String moodEmoji = entry != null ? entry.getMood() : "";

                Button dayButton = new Button(day + "\n" + moodEmoji);
                dayButton.setStyle("""
                -fx-background-color: #F3E5F5;
                -fx-text-fill: #4A148C;
                -fx-font-weight: bold;
                -fx-background-radius: 12;
                -fx-border-radius: 12;
                -fx-font-size: 13px;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 2, 0.2, 1, 1);
            """);
                dayButton.setPrefSize(70, 70);
                int col = (firstDayOfWeek - 1 + day - 1) % 7;
                int row = ((firstDayOfWeek - 1 + day - 1) / 7) + 1; // +1 because row 0 has weekdays
                calendarGrid.add(dayButton, col, row);

                LocalDate finalDate = date;
                String finalMood = moodEmoji;

                dayButton.setOnMouseEntered(e -> dayButton.setStyle("""
                -fx-background-color: #E1BEE7;
                -fx-text-fill: #4A148C;
                -fx-font-weight: bold;
                -fx-background-radius: 12;
                -fx-border-radius: 12;
                -fx-font-size: 13px;
                -fx-effect: dropshadow(gaussian, #CE93D8, 3, 0.3, 2, 2);
            """));

                dayButton.setOnMouseExited(e -> dayButton.setStyle("""
                -fx-background-color: #F3E5F5;
                -fx-text-fill: #4A148C;
                -fx-font-weight: bold;
                -fx-background-radius: 12;
                -fx-border-radius: 12;
                -fx-font-size: 13px;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 2, 0.2, 1, 1);
            """));

                dayButton.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(entry != null ? "Mood Details" : "No Mood Logged");
                    alert.setHeaderText("📅 " + finalDate + (finalMood.isEmpty() ? "" : " - " + finalMood));
                    alert.setContentText(entry != null && !entry.getNotes().isEmpty() ?
                            entry.getNotes() : "No notes for this day.");
                    alert.showAndWait();
                });
            }
        };

        updateCalendar.run();

        prevMonth.setOnAction(e -> {
            currentDate[0] = currentDate[0].minusMonths(1);
            updateCalendar.run();
        });

        nextMonth.setOnAction(e -> {
            currentDate[0] = currentDate[0].plusMonths(1);
            updateCalendar.run();
        });
    }

}

