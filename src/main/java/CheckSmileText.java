
public class CheckSmileText implements Analyzer<String> {

    private final String[] smiles = {":)", ":(", "))", ":/", ":|"};

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
        for (String s : smiles) {
            if (message.contains(s)) {
                return FilterType.USE_SMILE;
            }
        }
        return FilterType.GOOD;
    }
}
