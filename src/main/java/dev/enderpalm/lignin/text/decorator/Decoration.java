package dev.enderpalm.lignin.text.decorator;

import dev.enderpalm.lignin.text.decorator.enums.DropShadow;
import dev.enderpalm.lignin.text.decorator.enums.Style;

public record Decoration(Style style, int primaryColor, int secondaryColor, int borderRadius, DropShadow dropShadow) {

    public static Decoration DEFAULT = new Decoration(Style.FILL_SOLID,0,0,0,DropShadow.NONE);
}
