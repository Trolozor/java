package headers;

import java.awt.*;

public interface Windows {
    int SIZE_SMALL_WINDOW_X = 500;
    int SIZE_SMALL_WINDOW_Y = 300;
    int SIZE_WINDOW_X = 1280;
    int SIZE_WINDOW_Y = 720;
    int SIZE_PANEL_X = SIZE_WINDOW_X;
    int SIZE_PANEL_Y = 70;
    double BUTTON_WIDTH_RATIO = 0.3;
    double BUTTON_HEIGHT_RATIO = 0.1;
    double SMALL_BUTTON = 0.5;

    default Dimension getButtonPreferredSize() {
        int buttonWidth = (int) (SIZE_WINDOW_X * BUTTON_WIDTH_RATIO);
        int buttonHeight = (int) (SIZE_WINDOW_Y * BUTTON_HEIGHT_RATIO);
        return new Dimension(buttonWidth, buttonHeight);
    }
    default Dimension getButtonPreferredSizeSmall() {
        int buttonWidth = (int) (SIZE_WINDOW_X * BUTTON_WIDTH_RATIO * SMALL_BUTTON);
        int buttonHeight = (int) (SIZE_WINDOW_Y * BUTTON_HEIGHT_RATIO * SMALL_BUTTON);
        return new Dimension(buttonWidth, buttonHeight);
    }

    default Dimension getWindowPreferredSize() {
        return new Dimension(SIZE_WINDOW_X, SIZE_WINDOW_Y);
    }
}

