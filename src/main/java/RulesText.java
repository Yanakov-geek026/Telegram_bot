import java.util.List;

public class RulesText implements AnalyzerText {

    @Override
    public AnalyzerText smileCheckTextAnalyzer() {
        return (AnalyzerText) new CheckSmileText();
    }

    @Override
    public AnalyzerText longCheckTextAnalyzer(int textSize) {
        return (AnalyzerText) new CheckLongText(textSize);
    }

    @Override
    public AnalyzerText phoneNumberCheckTextAnalyzer() {
        return (AnalyzerText) new CheckPhoneNumberText();
    }

    @Override
    public AnalyzerText linkCheckTextAnalyzer() {
        return (AnalyzerText) new CheckLinkText();
    }

    public FilterType TextFilter(String text, List<AnalyzerText> analyzerText) {
        return AnalyzerText.super.TextAnalyzer(text, analyzerText);
    }

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
        return null;
    }

}
