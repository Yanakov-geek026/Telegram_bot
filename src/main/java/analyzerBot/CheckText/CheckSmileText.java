package analyzerBot.CheckText;

import analyzerBot.AnalyzerInterface.ControlRules;
import analyzerBot.Types.FilterType;

import java.io.Serializable;

public class CheckSmileText extends ControlRules<String> implements Serializable {

    private final String[] smiles = {":)", ":(", "))", ":|"};

    public CheckSmileText() {
        this.ruleBoolean = true;
    }

    @Override
    public boolean check(String message) {
        for (String s : smiles) {
            if (message.contains(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.USE_SMILE;
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
        return (ruleBoolean? "(ON)": "(OFF)") + " You can not use emoticons " + FilterType.USE_SMILE + "\n";
    }
}
