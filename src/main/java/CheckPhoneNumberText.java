    import java.util.regex.Matcher;
    import java.util.regex.Pattern;

    public class CheckPhoneNumberText implements Analyzer<String> {

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
            String regexPhoneNumber = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
            Pattern pattern = Pattern.compile(regexPhoneNumber);
            Matcher method = pattern.matcher(message);

            while (method.find()) {
                return FilterType.PHONE_NUMBER;
            }
            return FilterType.GOOD;
        }
    }
