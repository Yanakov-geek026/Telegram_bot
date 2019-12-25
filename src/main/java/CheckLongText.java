
public class CheckLongText implements Analyzer {

    private final int textSize;

    public CheckLongText(int textSize) {
        this.textSize = textSize;
    }

    @Override
    public FilterType TextAnalyzer(String text) {
        if (text.length() > textSize) {
            return FilterType.LONG_TEXT;
        }
        return FilterType.GOOD;
    }
}
