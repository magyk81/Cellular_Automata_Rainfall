package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class TreeCell extends Cell {
    private final Random rand;
    private final int WATER_MIN = 0, WATER_MAX = 10,
            WATER_MATURE_MIN = 2, WATER_MATURE_MAX = 10,
            WATER_SAPLING_MIN = 4, WATER_SAPLING_MAX = 8;
    private int water = WATER_MAX / 2;
    boolean mature = true;
    TreeCell(int x, int y, double size) {
        super(x, y, size);

        rand = new Random();
        taoNew = tao = (int) GRAYSCALE_TAO_MAX / 2;

        tao = Math.min((int) GRAYSCALE_TAO_MAX, Math.max(0, ((int) (rand.nextGaussian() + tao))));
        update();
    }

    @Override
    int evolve(int feedfront) {
        int activeNeigbors = 0;
        for (Cell neighbor : neighbors) {
            if (((TreeCell) neighbor).mature) activeNeigbors++;
        }

        if (mature) {
            if (water < WATER_MATURE_MIN || water > WATER_MATURE_MAX) taoNew--;
            else taoNew++;
        } else if (taoNew > 0) {
            if (water < WATER_SAPLING_MIN || water > WATER_SAPLING_MAX) taoNew--;
            else taoNew++;
        } else if (activeNeigbors >= 3) taoNew++;

        if (mature) {
            if (rand.nextFloat() < 0.1F) water--;
        } else if (rand.nextFloat() < 0.2F) water--;
        if (water < WATER_MIN) water = WATER_MIN;

        if (feedfront == 2) gotRain();

        if (mature) {
            if (feedfront == 0) return 2; // Sunny weather
            else if (feedfront == 1) return 1; // Cloudy weather
            return 0; // Rainy weather
        }
        return 0; // Saplings don't transpire vapor.
    }

    @Override
    void update() {
        if (taoNew > GRAYSCALE_TAO_MAX) taoNew = (int) GRAYSCALE_TAO_MAX;
        else if (taoNew < 0) taoNew = 0;
        tao = taoNew;

        if (tao == GRAYSCALE_TAO_MAX) mature = true;
        else if (tao == 0) mature = false;
    }

    @Override
    void draw(GraphicsContext context, double canvasOffset) {
        if (mature) {
            Color color = Color.color(0.5 - (0.3 * tao / GRAYSCALE_TAO_MAX), 0.7, 0.1 + (0.5 * tao / GRAYSCALE_TAO_MAX));
            context.setFill(color);
            context.fillRect(xDraw + canvasOffset, yDraw, size, size);
        } else {
            Color color = Color.color(0.9, 0.8, 0.5);
            context.setFill(color);
            context.fillRect(xDraw + canvasOffset, yDraw, size, size);
            context.setFill(Color.color(0.2, 0.6, 0.1));
            double ovalSize = tao / GRAYSCALE_TAO_MAX * size;
            context.fillRect(xDraw + canvasOffset - (ovalSize - size) / 2, yDraw - (ovalSize - size) / 2, ovalSize, ovalSize);
        }
    }

    private void gotRain() {
        water++;
        if (water > WATER_MAX) water = WATER_MAX;
    }

    public void clearCut() {
        tao = taoNew = 0;
        water = 0;
    }
}
