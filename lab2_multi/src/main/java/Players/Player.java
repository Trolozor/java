package Players;

import java.awt.*;
import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int score;
    private int hits;
    private String wins = "0";
    private int xPosition = 0;
    private int moveStep = 5;
    private int playerSize = 30;
    private long lastShotTime;
    private long cooldownMillis;
    private Color color;
    private int ID;
    private int x;
    private int y;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.hits = 0;
        this.cooldownMillis = 1000;
        this.lastShotTime = 0;
        color = Color.GRAY;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return color;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public int getXPosition() {
        return xPosition;
    }
    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }
    public int getID() {
        return ID;
    }
    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void increaseHits() {
        hits++;
    }
    public int getPlayerSize() {
        return playerSize;
    }

    public void increaseScore(int points) {
        score += points;
    }
    public Graphics draw(Graphics g, int width, int height) {

        int[] xPoints = {xPosition + width / 2, width / 2 - playerSize + xPosition, + width / 2 + playerSize + xPosition};
        int[] yPoints = {0, height, height};
        x = xPosition + width / 2;
        y = 0;

        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, 3);
        return g;
    }
    public boolean canShoot() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastShotTime) >= cooldownMillis; // Проверяем прошло ли достаточно времени с момента последнего выстрела
    }
    public void move(boolean isLeft, int width) {
        if(isLeft) {
            xPosition -= moveStep;
            width = -1 * width / 2;

            if (xPosition < width + playerSize) {
                xPosition = width + playerSize;
            }
        } else {
            xPosition += moveStep;
            width = width / 2;

            if (xPosition > width - playerSize) {
                xPosition = width - playerSize;
            }
        }
    }
    public Arrow shoot() {
        if(canShoot()) {
            Arrow arrow = new Arrow(x, y);
            lastShotTime = System.currentTimeMillis();
            return arrow;
        }
        return null;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }
}

