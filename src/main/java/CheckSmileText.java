
public class CheckSmileText implements AnalyzerText {

    private final String[] smiles = {":)", ":(", "))", ":/", ":|"};

    @Override
    public FilterType TextAnalyzer(String text) {
        for (String s : smiles) {
            if (text.contains(s)) {
                return FilterType.USE_SMILE;
            }
        }
        return FilterType.GOOD;
    }
}
