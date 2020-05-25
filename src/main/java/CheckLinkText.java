import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckLinkText implements Analyzer {

    @Override
    public FilterType TextAnalyzer(String text) {
        String regexLink = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(regexLink);
        Matcher method = pattern.matcher(text);

        while (method.find()) {
            return FilterType.REPOST_LINK;
        }
        return FilterType.GOOD;
    }
}
