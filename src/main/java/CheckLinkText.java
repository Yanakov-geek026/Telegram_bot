import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class  CheckLinkText implements Analyzer<String> {

    @Override
    public boolean check(String content) {
        return false;
    }

    @Override
    public TypeMessage getContentType() {
        return null;
    }

    @Override
    public FilterType getFilterType(String message) {
        String regexLink = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(regexLink);
        Matcher method = pattern.matcher(message);

        while (method.find()) {
            return FilterType.REPOST_LINK;
        }
        return FilterType.GOOD;
    }
}
