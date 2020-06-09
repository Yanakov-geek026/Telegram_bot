package analyzerBot.CheckText;

import analyzerBot.AnalyzerInterface.ControlRules;
import analyzerBot.Types.FilterType;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckPhoneNumberText extends ControlRules<String> implements Serializable {

    private Pattern regexPhoneNumber = Pattern.compile("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}");

    public CheckPhoneNumberText() {
        ruleBoolean = true;
    }

    @Override
    public boolean check(String message) {
        Matcher matcher = regexPhoneNumber.matcher(message);
        return matcher.find();
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.PHONE_NUMBER;
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
        return (ruleBoolean? "(ON)": "(OFF)") + " Cannot send phone number " + FilterType.PHONE_NUMBER + "\n";
    }
}
