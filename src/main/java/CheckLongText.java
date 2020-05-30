public class CheckLongText implements AnalyzerText {

    private final int textSize;

    CheckLongText(int textSize) {
        this.textSize = textSize;
    }

    @Override
    public boolean check(String message) {
        return message.length() > textSize;
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.LONG_TEXT;
    }
}
