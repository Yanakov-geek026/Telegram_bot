package analyzerBot.CheckText;

import analyzerBot.AnalyzerInterface.AnalyzerText;
import analyzerBot.Types.FilterType;

import java.io.Serializable;

public class CheckLongText implements AnalyzerText, Serializable {

    private final int textSize;

    public CheckLongText(int textSize) {
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
