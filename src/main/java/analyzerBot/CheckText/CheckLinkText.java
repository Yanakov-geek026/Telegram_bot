package analyzerBot.CheckText;

import analyzerBot.AnalyzerInterface.AnalyzerText;
import analyzerBot.Types.FilterType;

import java.io.Serializable;

public class CheckLinkText implements AnalyzerText, Serializable {

    private final String link = "https";

    @Override
    public boolean check(String message) {
        return message.contains(link);
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.REPOST_LINK;
    }
}
