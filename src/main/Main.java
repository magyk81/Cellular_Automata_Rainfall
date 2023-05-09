package main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        new SimulationTimer(cloudSimulation, treeSimulation).start();
    }

    public static void main(String[] args) {
        launch();
    }
}