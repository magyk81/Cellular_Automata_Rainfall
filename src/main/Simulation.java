package main;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Simulation {
    private final int RATE = 1;
    private final int SIZE = 100;
    private final double canvasOffset;
    private final int WIND_RATE = 1;
    private int wind = -1;
    private Cell[][] cells;
    int[][] feedback;
    private GraphicsContext context;
    private int framesPassed = 0;
    Simulation(double canvasSize, double canvasOffset, Group group, Cell.CellType cellType) {
        double cellSize = canvasSize / SIZE;
        this.canvasOffset = canvasOffset;

        cells = new Cell[SIZE][];
        feedback = new int[SIZE][];
        for (int i = 0; i < SIZE; i++) {
            cells[i] = new Cell[SIZE];
            feedback[i] = new int[SIZE];
            for (int j = 0; j < SIZE; j++) {
                if (cellType == Cell.CellType.CLOUD) cells[i][j] = new CloudCell(i, j, cellSize);
                else if (cellType == Cell.CellType.TREE) cells[i][j] = new TreeCell(i, j, cellSize);
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
        if (cellType == Cell.CellType.CLOUD) wind = WIND_RATE;

        Canvas canvas = new Canvas();
        canvas.setWidth(canvasSize + canvasOffset);
        canvas.setHeight(canvasSize);
        context = canvas.getGraphicsContext2D();
        group.getChildren().add(canvas);
    }

    public void handle(int[][] feedfront) {
        framesPassed++;
        if (framesPassed >= RATE) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    feedback[i][j] = cells[i][j].evolve(feedfront[i][j]);
                }
            }

            if (wind >= 0) {
                wind--;
                if (wind == 0) {
                    int[][] tmp = new int[SIZE][];
                    for (int i = 0; i < SIZE; i++) {
                        tmp[i] = new int[SIZE];
                        for (int j = 0; j < SIZE; j++) {
                            int _i = (i == SIZE - 1) ? 0 : i + 1;
                            int _j = (j == SIZE - 1) ? 0 : j + 1;
                            tmp[i][j] = cells[_i][_j].taoNew;
                        }
                    }
                    for (int i = 0; i < SIZE; i++) {
                        for (int j = 0; j < SIZE; j++) {
                            cells[i][j].taoNew = tmp[i][j];
                        }
                    }
                    wind = WIND_RATE;
                }
            }

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    cells[i][j].update();
                    cells[i][j].draw(context, canvasOffset);
                }
            }

            framesPassed = 0;
        }
    }
}
