package analyzerBot.CheckText;

import analyzerBot.AnalyzerInterface.AnalyzerText;
import analyzerBot.Types.FilterType;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckPhoneNumberText implements AnalyzerText, Serializable {

    private Pattern regexPhoneNumber = Pattern.compile("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}");

    @Override
    public boolean check(String message) {
        Matcher matcher = regexPhoneNumber.matcher(message);
        return matcher.find();
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.PHONE_NUMBER;
    }
}
