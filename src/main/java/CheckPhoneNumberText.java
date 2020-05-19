    import java.util.regex.Matcher;
    import java.util.regex.Pattern;

    public class CheckPhoneNumberText implements AnalyzerText {

        @Override
        public FilterType TextAnalyzer(String text) {

            String regexPhoneNumber = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
            Pattern pattern = Pattern.compile(regexPhoneNumber);
            Matcher method = pattern.matcher(text);

            while (method.find()) {
                return FilterType.PHONE_NUMBER;
            }
            return FilterType.GOOD;
        }
    }
