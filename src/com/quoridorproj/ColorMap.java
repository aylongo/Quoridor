package com.quoridorproj;

import java.awt.*;
import java.util.EnumMap;

enum ColorEnum {
    EMPTY_COLOR,
    WALL_COLOR,
    PLACED_WALL_COLOR,
    PLAYER1_COLOR,
    PLAYER2_COLOR,
    VALID_MOVE_PLAYER1_COLOR,
    VALID_MOVE_PLAYER2_COLOR;
}

public class ColorMap {
    private EnumMap<ColorEnum, Color> colorMap;

    /**
     * ColorMap Class Constructor
     */
    public ColorMap() {
        this.colorMap = new EnumMap<ColorEnum, Color>(ColorEnum.class);

        // Adds the colors to the HashMap
        this.colorMap.put(ColorEnum.EMPTY_COLOR, new Color(224, 224, 224));
        this.colorMap.put(ColorEnum.WALL_COLOR, new Color(192, 192, 192));
        this.colorMap.put(ColorEnum.PLACED_WALL_COLOR, new Color(105, 105, 105, 255));
        this.colorMap.put(ColorEnum.PLAYER1_COLOR, new Color(255, 64, 64));
        this.colorMap.put(ColorEnum.PLAYER2_COLOR, new Color(51, 153, 255));
        this.colorMap.put(ColorEnum.VALID_MOVE_PLAYER1_COLOR, new Color(255, 195, 139));
        this.colorMap.put(ColorEnum.VALID_MOVE_PLAYER2_COLOR, new Color(198, 226, 255));
    }

    /**
     * Returns the Color matched to the given enum key
     *
     * @param colorEnum A key for a color in the HashMap
     * @return The Color matched to the given enum key
     */
    public Color get(ColorEnum colorEnum) {
        return this.colorMap.get(colorEnum);
    }
}
