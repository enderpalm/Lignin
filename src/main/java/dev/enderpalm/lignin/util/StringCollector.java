package dev.enderpalm.lignin.util;

import org.jetbrains.annotations.Nullable;

public class StringCollector {
    private boolean isNotFirst;
    private final StringBuilder builder = new StringBuilder("{");

    private void separate() {
        if (this.isNotFirst) builder.append(',');
        this.isNotFirst = true;
    }

    public StringCollector collect(String string, @Nullable Object object) {
        if (object != null) {
            this.separate();
            builder.append(string);
            builder.append('=');
            builder.append(object);
        }
        return this;
    }

    public StringBuilder terminate(){
        return builder.append('}');
    }
}
