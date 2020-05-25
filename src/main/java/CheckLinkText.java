import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class  CheckLinkText implements AnalyzerText {

    @Override
    public boolean check(String message) {
        String regexLink = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(regexLink);
        Matcher method = pattern.matcher(message);

        return method.find();
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.REPOST_LINK;
    }
}
