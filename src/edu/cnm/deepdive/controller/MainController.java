package edu.cnm.deepdive.controller;

import edu.cnm.deepdive.model.Terrain;
import edu.cnm.deepdive.view.TerrainView;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;

public class MainController {

  @FXML private Slider speed;
  @FXML private Button resetButton;
  @FXML private TerrainView terrainView;
  @FXML private ToggleButton runToggle;
  @FXML private Slider populationSize;
  private boolean running;
  private Terrain terrain;
  private AnimationTimer timer;

  @FXML
  private void reset() {
    int newPopulation = (int) populationSize.getValue();
    terrain = new Terrain(newPopulation, new Random());
    terrainView.draw(terrain.getPatches());
  }

  @FXML
  private void initialize() {
    terrain = new Terrain(2, new Random());
    timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        terrainView.draw(terrain.getPatches());
      }
    };
    reset();
  }

  @FXML
  private void toggleRun(ActionEvent actionEvent) {
    if(runToggle.isSelected()) {
      start();
    } else {
      stop();
    }
  }

  private void start() {
    running = true;
    resetButton.setDisable(true);
    timer.start();
    new Runner().start();

  }

  public void stop() {
    runToggle.setDisable(true);
    resetButton.setDisable(false);
    running = false;
    timer.stop();
  }

  private class Runner extends Thread {

    @Override
    public void run() {
      while (running) {
        int speedSelection = (int) speed.getValue() * -1;
        for (int i = 0; i < 10; i++) {
          terrain.tick();
        }
        try {
          Thread.sleep(speedSelection);
        } catch (InterruptedException e) {
          // DO NOTHING
        }
      }
      Platform.runLater(() -> runToggle.setDisable(false));
    }
  }

}
