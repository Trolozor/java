package Players;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class Rounds implements Serializable {
    private int size;
    private int roundX;
    private int roundY;
    private Color color;
    private int speed;
    private String name;
    private int maxMovement;
    private static int panelWidth;

    public Rounds(int size, Color color, int roundX, int roundY, int speed, String name, int maxMovement) {
        this.roundY = roundY;
        this.roundX = roundX;
        this.size = size;
        this.color = color;
        this.speed = speed;
        this.name = name;
        this.maxMovement = maxMovement;
    }

    public static void setPanelWidth(int panelWidth) {
        Rounds.panelWidth = panelWidth;
    }

    public Graphics draw(Graphics g) {
        int innerCircleSize = size / 2;
        int innerCircleX = roundX + (size - innerCircleSize) / 2;
        int innerCircleY = roundY + (size - innerCircleSize) / 2;

        g.setColor(Color.BLACK);
        g.fillOval(roundX, roundY, size, size);

        g.setColor(color);
        g.fillOval(innerCircleX, innerCircleY, innerCircleSize, innerCircleSize);
        return g;
    }

    public void move(boolean reverse, boolean small) {
        if(reverse) {
            roundX -= speed;
            if (roundX > maxMovement - size || roundX < 0) {
                speed = -speed;
                roundX = Math.min(maxMovement, Math.max(0, roundX));
            }
        } else if(small) {
            roundX += speed;
            if (roundX < maxMovement + size || roundX > panelWidth - size) {
                speed = -speed;
                roundX = Math.min(panelWidth, Math.max(-panelWidth, roundX));
            }
        } else {
            roundX += speed;
            if (roundX > maxMovement || roundX < 0) {
                speed = -speed;
                roundX = Math.min(maxMovement, Math.max(-maxMovement, roundX));
            }
        }
    }

    public String getName() {
        return name;
    }
    public int getRoundX() {
        return roundX;
    }
    public void setRoundX(int roundX) {
        this.roundX = roundX;
    }

    public int getRoundY() { return roundY;}
    public void setRoundY(int roundY) {
        this.roundY = roundY;
    }

    public int getSize() { return size;}
    public void setSize(int size) {
        this.size = size;
    }
}
