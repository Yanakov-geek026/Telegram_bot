package analyzerBot.CheckPhoto;

import analyzerBot.AnalyzerInterface.ControlRules;
import analyzerBot.Types.FilterType;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.Serializable;
import java.util.List;

public class CheckWidthPhoto extends ControlRules<List<PhotoSize>> implements Serializable {

    private int width;

    public CheckWidthPhoto(int width) {
        ruleBoolean = true;
        this.width = width;
    }

    @Override
    public boolean check(List<PhotoSize> content) {
        for (PhotoSize photo : content) {
            if (photo.getWidth() > width) {
                return true;
            }
        }
        return false;
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.PHOTO_VERY_WIDTH;
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
        return (ruleBoolean? "(ON)": "(OFF)") + " Cannot send photos width than " + width + " pixels " + FilterType.PHOTO_VERY_WIDTH + "\n";
    }
}
