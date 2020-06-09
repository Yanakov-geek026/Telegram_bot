package analyzerBot.CheckPhoto;

import analyzerBot.AnalyzerInterface.ControlRules;
import analyzerBot.Types.FilterType;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.Serializable;
import java.util.List;

public class CheckHeightPhoto extends ControlRules<List<PhotoSize>> implements  Serializable {

    private int height;

    public CheckHeightPhoto(int height) {
        ruleBoolean = true;
        this.height = height;
    }

    @Override
    public boolean check(List<PhotoSize> content) {
        for (PhotoSize photo : content) {
            if (photo.getHeight() > height) {
                return true;
            }
        }
        return false;
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.PHOTO_VERY_HEIGHT;
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
        return (ruleBoolean? "(ON)": "(OFF)") + " Cannot send photos height than " + height + " pixels " + FilterType.PHOTO_VERY_WIDTH + "\n";
    }
}
