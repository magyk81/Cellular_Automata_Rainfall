package main;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cell {
    private int x, y;
    private double xDraw, yDraw;
    private int tao = 0;
    private Cell[] neighbors;
    private final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3,
            LEFT_UP = 4, LEFT_DOWN = 5, RIGHT_UP = 6, RIGHT_DOWN = 7;
    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        xDraw = x * Simulation.cellSize;
        yDraw = y * Simulation.cellSize;
    }

    void setNeighbors(Cell ...neighbors) {
        this.neighbors = neighbors;
    }

    void draw(GraphicsContext context) {
        context.setFill(Color.BLUE);
        context.fillRect(xDraw, yDraw, Simulation.cellSize, Simulation.cellSize);
    }
}
