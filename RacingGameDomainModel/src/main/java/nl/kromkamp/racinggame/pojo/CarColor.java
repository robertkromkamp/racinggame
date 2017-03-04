package nl.kromkamp.racinggame.pojo;

import java.util.Random;

public enum CarColor {
	RED, BLUE, GREEN, ORANGE, PURPLE, PINK, YELLOW, WHITE, BLACK;
	
	
	/**
     * Pick a random value of the CarColor enum.
     * @return a random CarColor.
     */
    public static CarColor getRandomColor() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
