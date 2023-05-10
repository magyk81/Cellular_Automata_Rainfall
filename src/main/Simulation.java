package main;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Simulation {
    private final int RATE = 1;
    private final int SIZE = 100;
    private final double canvasOffset;
    private final int WIND;
    private final int MAX_ITERATIONS = 1000;
    private int iterationsPassed = 0;
    private final int CLEAR_CUT_SIZE = 0;
    private Cell[][] cells;
    int[][] feedback;
    private GraphicsContext context;
    private int framesPassed = 0;
    private File[] outputFiles;
    private FileWriter[] fileWriters;
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

        if (cellType == Cell.CellType.TREE) {
            for (int i = (SIZE - CLEAR_CUT_SIZE) / 2; i < (SIZE + CLEAR_CUT_SIZE) / 2; i++) {
                for (int j = (SIZE - CLEAR_CUT_SIZE) / 2; j < (SIZE + CLEAR_CUT_SIZE) / 2; j++) {
                    ((TreeCell) cells[i][j]).clearCut();
                }
            }
            WIND = 0;
        } else WIND = 3;

        Canvas canvas = new Canvas();
        canvas.setWidth(canvasSize + canvasOffset);
        canvas.setHeight(canvasSize);
        context = canvas.getGraphicsContext2D();
        group.getChildren().add(canvas);

        if (cellType == Cell.CellType.CLOUD) {
            String filename = "cloud_count.txt";
            outputFiles = new File[1];
            outputFiles[0] = new File(filename);
            fileWriters = new FileWriter[1];
            try {
                fileWriters[0] = new FileWriter(filename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            String[] filenames = { "mature_count.txt", "sapling_count.txt" };
            outputFiles = new File[2];
            outputFiles[0] = new File(filenames[0]);
            outputFiles[1] = new File(filenames[1]);
            fileWriters =  new FileWriter[2];
            try {
                fileWriters[0] = new FileWriter(filenames[0]);
                fileWriters[1] = new FileWriter(filenames[1]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            for (File file : outputFiles) { file.createNewFile(); }
        } catch (IOException e) {
            System.out.println("output files already exist");
        }
    }

    public void close() throws IOException {
        for (FileWriter writer : fileWriters) { writer.close(); }
    }

    public boolean handle(int[][] feedfront) {
        framesPassed++;
        if (framesPassed >= RATE) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    feedback[i][j] = cells[i][j].evolve(feedfront[i][j]);
                }
            }

            if (WIND > 0) {
                int[][] tmp = new int[SIZE][];
                for (int i = 0; i < SIZE; i++) {
                    tmp[i] = new int[SIZE];
                    for (int j = 0; j < SIZE; j++) {
                        int _i = (i >= SIZE - WIND) ? i + WIND - SIZE : i + WIND;
                        int _j = (j >= SIZE - WIND) ? j + WIND - SIZE : j + WIND;
                        tmp[i][j] = cells[_i][_j].taoNew;
                    }
                }
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        cells[i][j].taoNew = tmp[i][j];
                    }
                }
            }

            int activeCellCount = 0, matureCellCount = 0, saplingCellCount = 0;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    cells[i][j].update();
                    cells[i][j].draw(context, canvasOffset);
                    if (cells[i][j].getClass() == CloudCell.class) {
                        if (cells[i][j].tao > 0) activeCellCount++;
                    } else {
                        if (((TreeCell) cells[i][j]).mature) matureCellCount++;
                        else if (cells[i][j].tao > 0) saplingCellCount++;
                    }
                }
            }
            try {
                if (cells[0][0].getClass() == CloudCell.class) {
                    fileWriters[0].write(activeCellCount + "\n");
                } else {
                    fileWriters[0].write(matureCellCount + "\n");
                    fileWriters[1].write(saplingCellCount + "\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            iterationsPassed++; System.out.println(iterationsPassed);
            framesPassed = 0;
            return iterationsPassed >= MAX_ITERATIONS;
        }
        return false;
    }
}
