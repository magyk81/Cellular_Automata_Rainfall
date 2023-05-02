package main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Group group = new Group();
        Scene scene = new Scene(group, 500, 500);
        stage.setTitle("Cellular Automata Rainfall");
        stage.setScene(scene);
        stage.show();

        Simulation simulation = new Simulation(scene.getWidth(), group);
        simulation.start();
    }

    public static void main(String[] args) {
        launch();
    }
}