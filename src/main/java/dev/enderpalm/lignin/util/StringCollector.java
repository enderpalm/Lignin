package dev.enderpalm.lignin.util;

import org.jetbrains.annotations.Nullable;

public class StringCollector {
    private boolean isNotFirst;
    private final StringBuilder builder = new StringBuilder("{");

    private void separate() {
        if (this.isNotFirst) builder.append(',');
        this.isNotFirst = true;
    }

    public StringCollector collectValue(String string, @Nullable Object object) {
        if (object != null) {
            this.separate();
            builder.append(string);
            builder.append('=');
            builder.append(object);
        }
        return this;
    }

    public StringCollector collectFlag(String string, @Nullable Boolean flag){
        if (flag != null) {
            this.separate();
            if (!flag) builder.append('!');
            builder.append(string);
        }
        return this;
    }

    public StringBuilder terminate(){
        return builder.append('}');
    }
}
