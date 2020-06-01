package analyzerBot.CheckText;

import analyzerBot.AnalyzerInterface.AnalyzerText;
import analyzerBot.Types.FilterType;

import java.io.Serializable;

public class CheckFindWordText implements AnalyzerText, Serializable {

    private String word;

    public CheckFindWordText(String word) {
        this.word = word;
    }

    @Override
    public boolean check(String message) {
        return message.contains(word);
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.FORBIDDEN_WORD;
    }
}
