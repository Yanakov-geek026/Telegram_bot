import java.util.List;

public interface AnalyzerText extends Analyzer<String>{

    AnalyzerText smileCheckTextAnalyzer();

    AnalyzerText longCheckTextAnalyzer(int textSize);

    AnalyzerText phoneNumberCheckTextAnalyzer();

    AnalyzerText linkCheckTextAnalyzer();

    default FilterType TextAnalyzer(String text, List<AnalyzerText> analyzerText) {

        for (Analyzer analyzer : analyzerText) {
            FilterType answer = analyzer.getFilterType(text);

            if (answer != FilterType.GOOD) {
                return answer;
            }
        }
        return FilterType.GOOD;
    }
}
