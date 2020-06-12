package ru.gasu.yanakov.bot.analyzer.controllers.check.video;

import org.telegram.telegrambots.meta.api.objects.Video;
import ru.gasu.yanakov.bot.analyzer.controllers.interfaces.ControlRules;
import ru.gasu.yanakov.bot.analyzer.publices.types.FilterType;

public class CheckSizeVideo extends ControlRules<Video> {

    private int width;
    private int height;

    public CheckSizeVideo(int width, int height) {
        ruleBoolean = true;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean check(Video video) {
        return ((video.getHeight() > height || video.getWidth() > width));
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.VIDEO_SIZE;
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
        return (ruleBoolean? "(ON)": "(OFF)") + " You cannot send a video larger than " +
                width + "x" + height + " pixels " + FilterType.VIDEO_SIZE + "\n";
    }
}
