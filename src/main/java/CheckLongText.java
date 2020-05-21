
public class CheckLongText implements Analyzer<String> {

    private final int textSize;

    public CheckLongText(int textSize) {
        this.textSize = textSize;
    }

    @Override
    public boolean check(String content) {
        return false;
    }

    @Override
    public TypeMessage getContentType() {
        return null;
    }

    @Override
    public FilterType getFilterType(String message) {
        if (message.length() > textSize) {
            return FilterType.LONG_TEXT;
        }
        return FilterType.GOOD;
    }
}
