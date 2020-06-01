package analyzerBot.CheckText;

import analyzerBot.AnalyzerInterface.AnalyzerText;
import analyzerBot.Types.FilterType;

import java.io.Serializable;

public class CheckSmileText implements AnalyzerText, Serializable {

    private final String[] smiles = {":)", ":(", "))", ":/", ":|"};

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
}
