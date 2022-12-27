package dev.enderpalm.lignin.util;

import dev.enderpalm.lignin.core.ClientEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;

public final class TimeHelper {

    private static final Calendar calendar = Calendar.getInstance();
    public TimeHelper(){
        calendar.setTime(new Date());
    }

    public boolean checkAnniversary(int month, int day){
        return getCurrentMnD().month == month && getCurrentMnD().day == day;
    }

    public @NotNull MonthAndDay getCurrentMnD(){
        return new MonthAndDay(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
    }

    public record MonthAndDay(int month, int day){}
}
