package analyzerBot.CheckText;

import analyzerBot.AnalyzerInterface.ControlRules;
import analyzerBot.Types.FilterType;

import java.io.Serializable;

public class CheckLongText extends ControlRules<String> implements Serializable {

    private final int textSize;

    public CheckLongText(int textSize) {
        ruleBoolean = true;
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
        return (ruleBoolean? "(ON)": "(OFF)") + " Cannot send message more than " + textSize + " characters " + FilterType.LONG_TEXT + "\n";
    }
}
