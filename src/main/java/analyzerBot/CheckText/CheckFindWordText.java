package analyzerBot.CheckText;

import analyzerBot.AnalyzerInterface.ControlRules;
import analyzerBot.Types.FilterType;

import java.io.Serializable;
import java.util.List;

public class CheckFindWordText extends ControlRules<String> implements Serializable {

    private List<String> forbiddenWords;

    public CheckFindWordText(List<String> forbiddenWords) {
        ruleBoolean = true;
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

    @Override
    public void onRule() {
        ruleBoolean = true;
    }

    @Override
    public void offRule() {
        ruleBoolean = false;
    }

    @Override
    public boolean checkActivateRule() {
        return ruleBoolean;
    }

    @Override
    public String manualRules() {
        return (ruleBoolean? "(ON)": "(OFF)") + " You can't use word a (" + forbiddenWords + ") " + FilterType.FORBIDDEN_WORD + "\n";
    }
}
