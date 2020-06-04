package analyzerBot.CheckText;

import analyzerBot.AnalyzerInterface.AnalyzerText;
import analyzerBot.Types.FilterType;

import java.io.Serializable;
import java.util.List;

public class CheckFindWordText implements AnalyzerText, Serializable {

    private List<String> forbiddenWords;

    public CheckFindWordText(List<String> forbiddenWords) {
        this.forbiddenWords = forbiddenWords;
    }

    @Override
    public boolean check(String message) {
        for (String word : forbiddenWords) {
            if (message.contains(word)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.FORBIDDEN_WORD;
    }
}
