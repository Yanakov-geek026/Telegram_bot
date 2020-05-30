public class CheckSmileText implements AnalyzerText {

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
