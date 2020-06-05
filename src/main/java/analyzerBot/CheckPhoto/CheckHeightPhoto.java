package analyzerBot.CheckPhoto;

import analyzerBot.AnalyzerInterface.AnalyzerPhoto;
import analyzerBot.Types.FilterType;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.Serializable;
import java.util.List;

public class CheckHeightPhoto implements AnalyzerPhoto, Serializable {

    private int height;

    public CheckHeightPhoto(int height) {
        this.height = height;
    }

    @Override
    public boolean check(List<PhotoSize> content) {
        for (PhotoSize photo : content) {
            if (photo.getWidth() > height) {
                return true;
            }
        }
        return false;
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.PHOTO_VERY_HEIGHT;
    }
}
