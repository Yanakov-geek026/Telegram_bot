package analyzerBot.CheckPhoto;

import analyzerBot.AnalyzerInterface.AnalyzerPhoto;
import analyzerBot.Types.FilterType;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.Serializable;
import java.util.List;

public class CheckWidthPhoto implements AnalyzerPhoto, Serializable {

    private int width;

    public CheckWidthPhoto(int width) {
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
}
