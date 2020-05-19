
public interface AnalyzerText extends Analyzer {

    AnalyzerText smileCheckTextAnalyzer();

    AnalyzerText longCheckTextAnalyzer(int textSize);

    AnalyzerText phoneNumberCheckTextAnalyzer();

    AnalyzerText linkCheckTextAnalyzer();

    FilterType TextAnalyzer(String text);
}
