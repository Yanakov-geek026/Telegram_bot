package analyzerBot.CheckText;

import analyzerBot.AnalyzerInterface.AnalyzerText;
import analyzerBot.Types.FilterType;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckPhoneNumberOrLink implements AnalyzerText, Serializable {

    private boolean number = false;
    private Pattern regexPhoneNumber = Pattern.compile("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}");
    private Pattern regexLink = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    @Override
    public boolean check(String message) {
        Matcher matcherPhoneNumber = regexPhoneNumber.matcher(message);
        Matcher matcherLink = regexLink.matcher(message);

        if (matcherPhoneNumber.find()) {
            number = true;
            return true;
        } else {
            return matcherLink.find();
        }
    }

    @Override
    public FilterType getFilterType() {
        return (number ? FilterType.PHONE_NUMBER : FilterType.REPOST_LINK);
    }
}
