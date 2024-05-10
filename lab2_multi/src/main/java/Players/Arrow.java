package Players;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Arrow extends JComponent implements Serializable {
    public static final int WIDTH = 5;
    public static final int HEIGHT = 20;
    private int x;
    private int y;
    private int speed = 20;
    private int ID;

    public Arrow(int x, int y) {
        this.x = x;
        this.y = y;
        setSize(WIDTH, HEIGHT);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void move() {
        y -= speed;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x - WIDTH/2, y, 4, HEIGHT);

        int[] xPoints = {x - 5, x + 5, x};
        int[] yPoints = {y, y, y - 10};
        g.setColor(Color.RED);
        g.drawPolygon(xPoints, yPoints, 3);
        g.fillPolygon(xPoints, yPoints, 3);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}

