package ru.gasu.yanakov.bot.analyzer.controllers.check.video;

import org.telegram.telegrambots.meta.api.objects.Video;
import ru.gasu.yanakov.bot.analyzer.controllers.interfaces.ControlRules;
import ru.gasu.yanakov.bot.analyzer.publices.types.FilterType;

public class CheckDurationVideo extends ControlRules<Video> {

    private final int durations;

    public CheckDurationVideo(int durations) {
        ruleBoolean = true;
        this.durations = durations;
    }

    @Override
    public void onRule() {
        ruleBoolean = true;
    }

    @Override
    public void offRule() {
        ruleBoolean = false;
    }

    @Override
    public boolean checkActivateRule() {
        return ruleBoolean;
    }

    @Override
    public String manualRules() {
        return (ruleBoolean? "(ON)": "(OFF)") + " You can not video more than " +
                durations + " seconds " + FilterType.VIDEO_DURATIONS + "\n";
    }

    @Override
    public boolean check(Video content) {
        return content.getDuration() > durations;
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.VIDEO_DURATIONS;
    }
}
