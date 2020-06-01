package analyzerBot.CheckPhoto;

import analyzerBot.AnalyzerInterface.AnalyzerPhoto;
import analyzerBot.Types.FilterType;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.Serializable;

public class CheckSizePhoto implements AnalyzerPhoto, Serializable {

    @Override
    public boolean check(PhotoSize content) {
        return false;
    }

    @Override
    public FilterType getFilterType() {
        return null;
    }
}
