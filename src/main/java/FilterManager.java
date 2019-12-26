import java.util.List;

public class FilterManager {

    private final List<Analyzer> filterManager;

    public FilterManager(List<Analyzer> filterManager) {
        this.filterManager = filterManager;
    }

    public FilterType analyzer(String message) {

        for (Analyzer analyzer : filterManager) {
            FilterType answer = analyzer.TextAnalyzer(message);

            if (answer != FilterType.GOOD) {
                return answer;
            }
        }
        return FilterType.GOOD;
    }
}
