package main;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Simulation extends AnimationTimer {
    private final int RATE = 20;
    private final int SIZE = 100;
    public static double cellSize;
    private Cell[][] cells;
    private GraphicsContext context;
    private int framesPassed = 0;
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
                int leftEdge = (i == 0) ? o : i - 1, upEdge = (j == 0) ? o : j - 1,
                        rightEdge = (i == o) ? 0 : i + 1, downEdge = (j == o) ? 0 : j + 1;
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
        framesPassed++;
        if (framesPassed >= RATE) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    cells[i][j].evolve();
                }
            }

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    cells[i][j].update();
                    cells[i][j].draw(context);
                }
            }

            framesPassed = 0;
        }
    }
}
