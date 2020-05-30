import org.telegram.telegrambots.meta.api.objects.PhotoSize;

public interface AnalyzerPhoto extends Analyzer<PhotoSize> {
    default TypeMessage getContentType() {
        return TypeMessage.PHOTO_MESSAGE;
    }
}
