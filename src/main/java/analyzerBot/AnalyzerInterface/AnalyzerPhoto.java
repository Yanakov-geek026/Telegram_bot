package analyzerBot.AnalyzerInterface;

import analyzerBot.Types.TypeMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

public interface AnalyzerPhoto extends Analyzer<List<PhotoSize>> {
    default TypeMessage getContentType() {
        return TypeMessage.PHOTO_MESSAGE;
    }
}
