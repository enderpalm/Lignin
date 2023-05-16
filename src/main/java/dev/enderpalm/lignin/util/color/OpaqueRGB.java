package dev.enderpalm.lignin.util.color;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class OpaqueRGB {

    private final byte red;
    private final byte green;
    private final byte blue;

    private static final char CODE_PREFIX = '#';

    public OpaqueRGB(int packed) {
       this(packed >> 16 & 0xff, packed >> 8 & 0xff, packed & 0xff);
    }

    public OpaqueRGB(int red, int green, int blue) {
        this.red = (byte) red;
        this.green = (byte) green;
        this.blue = (byte) blue;
    }

    public int getPacked() {
        return this.getRed() << 16 | this.getGreen() << 8 | this.getBlue();
    }

    public int getRed() {
        return Byte.toUnsignedInt(this.red);
    }

    public int getGreen() {
        return Byte.toUnsignedInt(this.green);
    }

    public int getBlue() {
        return Byte.toUnsignedInt(this.blue);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof OpaqueRGB) {
            return this.red == ((OpaqueRGB) object).red && this.green == ((OpaqueRGB) object).green && this.blue == ((OpaqueRGB) object).blue;
        } else return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.red, this.green, this.blue);
    }

    @Override
    public String toString() {
        return CODE_PREFIX + Integer.toHexString(this.getPacked());
    }

    public static @Nullable OpaqueRGB parseColor(@Nullable String string) {
        if (string == null) return null;
        try {
            if (string.startsWith(String.valueOf(CODE_PREFIX)))
                return new OpaqueRGB(Integer.parseInt(string.substring(1), 16));
            return new OpaqueRGB(Integer.parseInt(string, 10));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid opaque color: " + string);
        }
    }
}
