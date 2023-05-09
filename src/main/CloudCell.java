package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class CloudCell extends Cell {
    private final Random rand;
    CloudCell(int x, int y, double size) {
        super(x, y, size);

        rand = new Random();
        tao = Math.min((int) GRAYSCALE_TAO_MAX, Math.max(0, ((int) (rand.nextGaussian() + 1))));
        taoNew = tao;
    }

    @Override
    int evolve(int feedfront) {
        int activeNeigbors = 0;
        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i].tao > 0) activeNeigbors++;
        }
        if (tao <= 0) {
            if (activeNeigbors == 3) {
                float chance = rand.nextFloat();
                if (chance > 0.2F) taoNew = 1 + feedfront; // More vapor = more lifetime
            }
        } else if (activeNeigbors < 2 || activeNeigbors > 3) {
            float chance = rand.nextFloat();
            if (chance > 0.2F) {
                taoNew = tao - 1;
                return 2; // Rainy weather
            } else if (chance < 0.05F) {
                taoNew = 1 + feedfront; // More vapor = more lifetime
            }
        }

        if (tao > 0) return 1; // Cloudy weather
        return 0; // Sunny weather
    }

    @Override
    void update() { tao = taoNew; }

    @Override
    void draw(GraphicsContext context, double canvasOffset) {
        if (tao == 0) context.setFill(Color.SKYBLUE);
        else context.setFill(Color.gray(1.0 + (1.0 / GRAYSCALE_TAO_MAX) - ((double) tao / GRAYSCALE_TAO_MAX)));
        context.fillRect(xDraw + canvasOffset, yDraw, size, size);
    }
}
