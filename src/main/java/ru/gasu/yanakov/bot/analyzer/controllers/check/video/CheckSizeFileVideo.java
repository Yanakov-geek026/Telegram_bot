package ru.gasu.yanakov.bot.analyzer.controllers.check.video;

import org.telegram.telegrambots.meta.api.objects.Video;
import ru.gasu.yanakov.bot.analyzer.controllers.interfaces.ControlRules;
import ru.gasu.yanakov.bot.analyzer.publices.types.FilterType;

public class CheckSizeFileVideo extends ControlRules<Video> {

    private final int sizeFileVideo;

    public CheckSizeFileVideo(int sizeFileVideo) {
        ruleBoolean = true;
        this.sizeFileVideo = sizeFileVideo;
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
                sizeFileVideo + " seconds " + FilterType.VIDEO_FILE_SIZE + "\n";
    }

    @Override
    public boolean check(Video content) {
        return content.getDuration() > sizeFileVideo;
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.VIDEO_FILE_SIZE;
    }
}
