package ru.gasu.yanakov.bot.analyzer.controllers.check.photo;

import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import ru.gasu.yanakov.bot.analyzer.controllers.interfaces.ControlRules;
import ru.gasu.yanakov.bot.analyzer.publices.types.FilterType;

import java.util.List;

public class CheckFileSizePhoto extends ControlRules<List<PhotoSize>> {

    private final int sizeFile;

    public CheckFileSizePhoto(int sizeFile) {
        ruleBoolean = true;
        this.sizeFile = sizeFile;
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
        return (ruleBoolean? "(ON)": "(OFF)") + " You can not send photos more than " +
                sizeFile + " mb " + FilterType.PHOTO_FILE_SIZE + "\n";
    }

    @Override
    public boolean check(List<PhotoSize> content) {
        for (PhotoSize photo : content) {
            if (photo.getFileSize() > sizeFile) {
                return true;
            }
        }
        return false;
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.PHOTO_FILE_SIZE;
    }
}
