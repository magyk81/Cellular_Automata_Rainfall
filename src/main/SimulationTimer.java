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
        boolean done = false;
        if (cloudSimulation.handle(treeSimulation.feedback)) done = true;
        if (treeSimulation.handle(cloudSimulation.feedback)) done = true;
        if (done) stop();
    }
}
