package org.example.ecloset.mapper;

import org.example.ecloset.enums.Color;
import org.springframework.stereotype.Component;

@Component
public class ColorMapper {

    public Color fromHex(String hex) {
        if (hex == null || hex.length() < 7) return null;
        try {
            int r = Integer.parseInt(hex.substring(1, 3), 16);
            int g = Integer.parseInt(hex.substring(3, 5), 16);
            int b = Integer.parseInt(hex.substring(5, 7), 16);

            float[] hsv = java.awt.Color.RGBtoHSB(r, g, b, null);
            float hue = hsv[0] * 360;
            float sat = hsv[1];
            float val = hsv[2];

            // Ахроматические
            if (val < 0.15f) return Color.BLACK;
            if (val > 0.92f && sat < 0.08f) return Color.WHITE;
            if (sat < 0.12f) return Color.GRAY;

            // Бежевый: низкая насыщенность, высокая яркость, тёплый оттенок
            if (sat < 0.30f && val > 0.65f && hue >= 25 && hue < 60) return Color.BEIGE;
            // Коричневый: тёмный, тёплый
            if (val < 0.55f && sat >= 0.25f && hue >= 10 && hue < 50) return Color.BROWN;

            // Цветные по оттенку
            if (hue < 15 || hue >= 345) return Color.RED;
            if (hue < 45) return Color.ORANGE;
            if (hue < 70) return Color.YELLOW;
            if (hue < 170) return Color.GREEN;
            if (hue < 260) {
                // Тёмно-синий = navy
                return val < 0.45f ? Color.NAVY : Color.BLUE;
            }
            if (hue < 290) return Color.PURPLE;
            if (hue < 345) return Color.PINK;
            return Color.GRAY;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}