package analyzerBot.CheckText;

import analyzerBot.AnalyzerInterface.ControlRules;
import analyzerBot.Types.FilterType;

import java.io.Serializable;

public class CheckLinkText extends ControlRules<String> implements Serializable {

    public CheckLinkText() {
        ruleBoolean = true;
    }

    @Override
    public boolean check(String message) {
        String link = "https";
        return message.contains(link);
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.REPOST_LINK;
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
        return (ruleBoolean? "(ON)": "(OFF)") + " Do not send various links " + FilterType.REPOST_LINK + "\n";
    }
}
