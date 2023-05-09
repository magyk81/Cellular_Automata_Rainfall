package main;

import javafx.animation.AnimationTimer;

public class SimulationTimer extends AnimationTimer {
    Simulation cloudSimulation, treeSimulation;
    SimulationTimer(Simulation cloudSimulation, Simulation treeSimulation) {
        this.cloudSimulation = cloudSimulation;
        this.treeSimulation = treeSimulation;
    }
    @Override
    public void handle(long l) {
        cloudSimulation.handle(treeSimulation.feedback);
        treeSimulation.handle(cloudSimulation.feedback);
    }
}
