package dev.enderpalm.lignin.text.decorator.enums;

import com.mojang.math.Vector3d;
import org.jetbrains.annotations.NotNull;

public enum DropShadow {
    UP(toVector(0,1)),
    DOWN(toVector(0,-1)),
    LEFT(toVector(-1,0)),
    RIGHT(toVector(1,0)),
    RIGHT_UP(toVector(1,1)),
    RIGHT_DOWN(toVector(1,-1)),
    LEFT_DOWN(toVector(-1,-1)),
    LEFT_UP(toVector(-1,1));

    private final Vector3d translate;

    DropShadow(Vector3d translate){
        this.translate = translate;
    }

    public Vector3d getTranslate() {
        return this.translate;
    }

    private static @NotNull Vector3d toVector(int x, int y){
        return new Vector3d(x, y, 0.0d);
    }
}
