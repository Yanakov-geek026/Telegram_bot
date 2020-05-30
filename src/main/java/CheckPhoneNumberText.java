import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckPhoneNumberText implements AnalyzerText {

    @Override
    public boolean check(String message) {
        String regexPhoneNumber = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
        Pattern pattern = Pattern.compile(regexPhoneNumber);
        Matcher method = pattern.matcher(message);

        return method.find();
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.PHONE_NUMBER;
    }
}
