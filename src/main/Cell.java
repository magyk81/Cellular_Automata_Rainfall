package main;

import javafx.scene.canvas.GraphicsContext;

public abstract class Cell {
    public enum CellType { CLOUD, TREE }
    private int x, y;
    final double xDraw, yDraw, size;
    int tao, taoNew;
    public static double GRAYSCALE_TAO_MAX = 10.0;
    Cell[] neighbors;
    private final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3,
            LEFT_UP = 4, LEFT_DOWN = 5, RIGHT_UP = 6, RIGHT_DOWN = 7;
    Cell(int x, int y, double size) {
        this.x = x;
        this.y = y;
        this.size = size;
        xDraw = x * size;
        yDraw = y * size;
    }

    void setNeighbors(Cell ...neighbors) {
        this.neighbors = neighbors;
    }

    abstract int evolve(int feedfront);

    abstract void update();

    abstract void draw(GraphicsContext context, double canvasOffset);

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
