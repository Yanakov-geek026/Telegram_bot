
public interface Analyzer {

    static Analyzer smileCheckTextAnalyzer() {
        return new CheckSmileText();
    }

    static Analyzer longCheckTextAnalyzer(int textSize) {
        return new CheckLongText(textSize);
    }

    static Analyzer phoneNumberCheckTextAnalyzer() {
        return new CheckPhoneNumberText();
    }

    static Analyzer linkCheckTextAnalyzer() {
        return new CheckLinkText();
    }

    FilterType TextAnalyzer (String text);
}
