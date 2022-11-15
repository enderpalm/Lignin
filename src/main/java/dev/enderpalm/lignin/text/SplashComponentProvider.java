package dev.enderpalm.lignin.text;

import dev.enderpalm.lignin.Lignin;
import dev.enderpalm.lignin.util.TimeHelper;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Predicate;

import static dev.enderpalm.lignin.util.TimeHelper.MonthAndDay;

public class SplashComponentProvider {

    private final TimeHelper time;

    public SplashComponentProvider() {
        this.time = new TimeHelper();
    }

    public boolean forceShowVanilla(){
        Set<MonthAndDay> eventTimeSet = Lignin.SPLASH_EVENT_MAP.keySet();
        Predicate<MonthAndDay> mnd = monthAndDay -> time.checkAnniversary(monthAndDay.month(), monthAndDay.day());
        return eventTimeSet.stream().noneMatch(mnd);
    }

    public SplashComponentProvider append(MonthAndDay mnd, Component splash){
        Lignin.SPLASH_EVENT_MAP.put(mnd, splash);
        return this;
    }

    public @NotNull Component getFormattedSplash(){
        System.out.println(this.time.getCurrentMnD());
        for (MonthAndDay md : Lignin.SPLASH_EVENT_MAP.keySet())
            System.out.println(Lignin.SPLASH_EVENT_MAP.get(md));
        return Lignin.SPLASH_EVENT_MAP.get(this.time.getCurrentMnD());
    }
}
