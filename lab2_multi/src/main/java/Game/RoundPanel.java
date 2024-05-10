package Game;

import headers.*;
import Players.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RoundPanel extends JPanel implements Windows {
    private ArrayList<Rounds> rounds = new ArrayList<>();
    private int sizeBigTarget = 90;
    private int sizeSmallTarget = 50;
    private Client client;
    private List<Arrow> arrows;
    private boolean start = false;

    public RoundPanel() {
        setPreferredSize(new Dimension(Windows.SIZE_PANEL_X, Windows.SIZE_PANEL_Y * 2));
        setBackground(Color.GRAY);
        arrows = new ArrayList<>();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                setTarget();
                Rounds.setPanelWidth(getWidth());
                client.setTargets(rounds);
                removeComponentListener(this);
            }
        });
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public void setTargets(Map<String, Integer> targets) {
        for (Map.Entry<String, Integer> entry : targets.entrySet()) {
            String name = entry.getKey();
            Integer coordinate = entry.getValue();
            for (Rounds target : rounds) {
                if (target.getName().equals(name)) {
                    target.setRoundX(coordinate);
                    repaint();
                    break;
                }
            }
        }
    }
    public ArrayList<Rounds> setTarget() {
        int bigTargetX = (getSize().width - sizeBigTarget) / 2;
        int bigTargetY = (getSize().height - sizeBigTarget);
        int smallTarget1X = (getSize().width) / 2 - sizeSmallTarget;
        int smallTarget2X = (getSize().width) / 2;
        int bigSpeed = 5;

        Rounds bigTarget = new Rounds(sizeBigTarget, Color.RED, bigTargetX, bigTargetY, bigSpeed, "big", getWidth() - sizeBigTarget);
        Rounds smallTarget1 = new Rounds(sizeSmallTarget, Color.RED, smallTarget1X, 0, bigSpeed * 2, "smallLeft", smallTarget1X + sizeSmallTarget);
        Rounds smallTarget2 = new Rounds(sizeSmallTarget, Color.RED, smallTarget2X, 0, bigSpeed * 2, "smallRight", smallTarget2X - sizeSmallTarget);

        rounds.add(bigTarget);
        rounds.add(smallTarget1);
        rounds.add(smallTarget2);
        return rounds;
    }

    public void addArrow(Arrow arrow) {
        arrows.add(arrow);
        System.out.println("X = " + arrow.getX() +"\t Y = " + arrow.getY());
        start = true;
    }
    public void updateArrows() {
        Iterator<Arrow> iterator = arrows.iterator();
        while (iterator.hasNext()) {
            Arrow arrow = iterator.next();
            arrow.move();
            if (arrow.getY() <= 0 || isCollision(arrow.getX(),arrow.getY(), arrow.getID())) {
                iterator.remove();
            }
        }
    }
    private boolean isCollision(int arrowX, int arrowY, int id) {
        for (Rounds round : rounds) {
            int roundX = round.getRoundX();
            int roundY = round.getRoundY();
            int roundSize = round.getSize();

            if (arrowX >= roundX && arrowX <= (roundX + roundSize) &&
                    arrowY >= roundY && arrowY <= (roundY + roundSize)) {
                if(round.getName().equals("big")) {
                    client.updatePlayer(1, id);
                } else {
                    client.updatePlayer(2, id);
                }
                return true;
            }
        }
        return false;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0, sizeSmallTarget / 2, getSize().width,  sizeSmallTarget / 2);
        g.drawLine(0, getHeight() - (sizeBigTarget / 2), getWidth(), getHeight() - (sizeBigTarget / 2));
        for(Rounds round : rounds){
            round.draw(g);
        }
        if(start) {
            for (Arrow arrow : arrows) {
                arrow.draw(g);
            }
        }
    }
}
