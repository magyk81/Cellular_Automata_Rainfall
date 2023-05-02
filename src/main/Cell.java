package main;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class Cell {
    private int x, y;
    private double xDraw, yDraw;
    private int tao, taoNew;
    private static double GRAYSCALE_TAO_MAX = 3.0;
    private Cell[] neighbors;
    private final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3,
            LEFT_UP = 4, LEFT_DOWN = 5, RIGHT_UP = 6, RIGHT_DOWN = 7;
    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        xDraw = x * Simulation.cellSize;
        yDraw = y * Simulation.cellSize;

        Random rand = new Random();
        tao = Math.min((int) GRAYSCALE_TAO_MAX, Math.max(0, ((int) (rand.nextGaussian() + 1))));
        taoNew = tao;
    }

    void setNeighbors(Cell ...neighbors) {
        this.neighbors = neighbors;
    }

    void evolve() {
        int activeNeigbors = 0;
        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i].tao > 0) activeNeigbors++;
        }
        if (tao <= 0) {
            if (activeNeigbors == 3) taoNew = 1;
        } else if (activeNeigbors < 2 || activeNeigbors > 3) {
            taoNew = tao - 1;
        }
    }

    void update() { tao = taoNew; }

    void draw(GraphicsContext context) {
        if (tao == 0) context.setFill(Color.SKYBLUE);
        else context.setFill(Color.gray(1.0 + (1.0 / GRAYSCALE_TAO_MAX) - ((double) tao / GRAYSCALE_TAO_MAX)));
        context.fillRect(xDraw, yDraw, Simulation.cellSize, Simulation.cellSize);
    }

    void printInfo() {
        System.out.print("SELF => "); printCoord();
        System.out.print("LEFT => "); neighbors[LEFT].printCoord();
        System.out.print("RIGHT => "); neighbors[RIGHT].printCoord();
        System.out.print("UP => "); neighbors[UP].printCoord();
        System.out.print("DOWN => "); neighbors[DOWN].printCoord();
        System.out.print("LEFT_UP => "); neighbors[LEFT_UP].printCoord();
        System.out.print("LEFT_DOWN => "); neighbors[LEFT_DOWN].printCoord();
        System.out.print("RIGHT_UP => "); neighbors[RIGHT_UP].printCoord();
        System.out.print("RIGHT_DOWN => "); neighbors[RIGHT_DOWN].printCoord();
    }

    private void printCoord() { System.out.println("x: " + x + ", y: " + y); }
}
