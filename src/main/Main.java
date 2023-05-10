package main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Group group = new Group();
        final int sceneHeight = 500;
        Scene scene = new Scene(group, sceneHeight * 2, sceneHeight);
        stage.setTitle("Cellular Automata Rainfall");
        stage.setScene(scene);
        stage.show();

        Simulation cloudSimulation = new Simulation(sceneHeight, 0, group, Cell.CellType.CLOUD);
        Simulation treeSimulation = new Simulation(sceneHeight, sceneHeight, group, Cell.CellType.TREE);
        SimulationTimer simTimer = new SimulationTimer(cloudSimulation, treeSimulation);
        simTimer.start();

        stage.setOnCloseRequest(event -> {
            try {
                cloudSimulation.close();
                treeSimulation.close();
                simTimer.stop();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}