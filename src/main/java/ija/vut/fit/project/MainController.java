package ija.vut.fit.project;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainController {

    @FXML
    private Pane paneContent;

    private List<Draw> contents = new ArrayList<>();
    private List<Updater> updates = new ArrayList<>();
    private Timer timer;
    private LocalTime time = LocalTime.parse("06:00:00");

    @FXML
    private TextField speedScale;

    @FXML
    private void onSpeedChange(){
        float scale = Float.parseFloat(speedScale.getText());
        timer.cancel();
        startRoute(scale);
    }

    @FXML
    private void onZoom(ScrollEvent event){
        event.consume();
        double zoom = event.getDeltaY();
        if (zoom > 0.0) zoom = 1.1;
        else zoom = 0.9;

        paneContent.setScaleX(zoom * paneContent.getScaleX());
        paneContent.setScaleY(zoom * paneContent.getScaleY());
    }

    public void setContents(List<Draw> contents) {
        this.contents = contents;
        for (Draw draw : contents){
            paneContent.getChildren().addAll(draw.getGUI());
            if (draw instanceof Updater){
                updates.add((Updater) draw);
            }
        }
    }

    public void startRoute(double scale){
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time = time.plusSeconds(1);
                for (Updater updater : updates){
                    Platform.runLater(() -> updater.update(time));
                }
            }
        }, 0, (long) (1000 / scale));

    }
}
