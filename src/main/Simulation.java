package main;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Simulation extends AnimationTimer {
    private final int SIZE = 100;
    public static double cellSize;
    private Cell[][] cells;
    private GraphicsContext context;
    Simulation(double canvasSize, Group group) {
        cellSize = canvasSize / SIZE;

        cells = new Cell[SIZE][];
        for (int i = 0; i < SIZE; i++) {
            cells[i] = new Cell[SIZE];
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int o = SIZE - 1;
                int leftEdge = (i == 0) ? o : i, upEdge = (j == 0) ? o : j,
                        rightEdge = (i == o) ? 0 : i, downEdge = (j == o) ? 0 : j;
                Cell left = cells[leftEdge][j], right = cells[rightEdge][j],
                        up = cells[i][upEdge], down = cells[i][downEdge],
                        leftUp = cells[leftEdge][upEdge], leftDown = cells[leftEdge][downEdge],
                        rightUp = cells[rightEdge][upEdge], rightDown = cells[rightEdge][downEdge];
                cells[i][j].setNeighbors(left, right, up, down, leftUp, leftDown, rightUp, rightDown);
            }
        }

        Canvas canvas = new Canvas();
        canvas.setWidth(canvasSize);
        canvas.setHeight(canvasSize);
        context = canvas.getGraphicsContext2D();
        group.getChildren().add(canvas);
    }

    @Override
    public void handle(long l) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j].draw(context);
            }
        }
    }
}
