package ru.gasu.yanakov.bot.analyzer.controllers.check.photo;

import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import ru.gasu.yanakov.bot.analyzer.controllers.interfaces.ControlRules;
import ru.gasu.yanakov.bot.analyzer.publices.types.FilterType;

import java.io.Serializable;
import java.util.List;

public class CheckSizePhoto extends ControlRules<List<PhotoSize>> implements Serializable {

    private int width;
    private int height;

    public CheckSizePhoto(int width, int height) {
        ruleBoolean = true;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean check(List<PhotoSize> content) {
        for (PhotoSize photo : content) {
            if (photo.getWidth() > width || photo.getHeight() > height) {
                return true;
            }
        }
        return false;
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.PHOTO_SIZE;
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
        return (ruleBoolean? "(ON)": "(OFF)") + " You cannot send a picture larger than " +
                width + "x" + height + " pixels " + FilterType.PHOTO_SIZE + "\n";
    }
}
