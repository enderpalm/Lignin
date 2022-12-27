package dev.enderpalm.lignin.text;

import dev.enderpalm.lignin.util.TimeHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static dev.enderpalm.lignin.util.TimeHelper.MonthAndDay;

@Environment(EnvType.CLIENT)
public final class SplashComponentBuilder {

    private final TimeHelper time;
    private final HashMap<MonthAndDay, Component> timedSplashComponent = new HashMap<>();
    private final Set<Component> randomSplashComponent = new HashSet<>();
    private boolean forceShowVanilla;
    private int shufflerIndex;

    public SplashComponentBuilder() {
        this.time = new TimeHelper();
    }

    public void prepareRandom(){
        RandomSource randomSource = RandomSource.create();
        this.forceShowVanilla = randomSource.nextFloat() > 1.0f
                || this.time.checkAnniversary(12,24)
                || this.time.checkAnniversary(1,1)
                || this.time.checkAnniversary(10,31);
        var size = this.randomSplashComponent.size();
        this.shufflerIndex = randomSource.nextInt(size != 0 ? size : 1);
    }

    private SplashComponentBuilder append(Component splash){
        this.randomSplashComponent.add(splash);
        return this;
    }

    private SplashComponentBuilder append(int month, int day, Component splash){
        this.timedSplashComponent.put(new MonthAndDay(month, day), splash);
        return this;
    }

    private Component getFallBack(String vanillaSplash){
        return Component.literal(vanillaSplash).withStyle(style ->
                style.withColor(ChatFormatting.YELLOW));
    }

    public @NotNull Component getFormattedSplash(String vanillaSplash){
        Predicate<TimeHelper.MonthAndDay> mnd = monthAndDay -> time.checkAnniversary(monthAndDay.month(), monthAndDay.day());
        var noValidDate = this.timedSplashComponent.keySet().stream().noneMatch(mnd);
        if (noValidDate) return forceShowVanilla || this.randomSplashComponent.isEmpty() ? getFallBack(vanillaSplash)
                : (Component) this.randomSplashComponent.toArray()[shufflerIndex];
        return Optional.ofNullable(this.timedSplashComponent.get(this.time.getCurrentMnD())).orElse(getFallBack(vanillaSplash));
    }
}
