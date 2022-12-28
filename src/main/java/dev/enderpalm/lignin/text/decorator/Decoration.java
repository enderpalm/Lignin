package dev.enderpalm.lignin.text.decorator;

import dev.enderpalm.lignin.text.decorator.enums.DropShadow;
import dev.enderpalm.lignin.text.decorator.enums.Style;

public record Decoration(Style style, int primaryColor, int secondaryColor, int borderRadius, DropShadow dropShadow) {

    Decoration(Style style, int color, int borderRadius, DropShadow dropShadow){
        this(style, color, 0, borderRadius, dropShadow);
    }
}
