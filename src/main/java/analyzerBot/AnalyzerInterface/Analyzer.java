package analyzerBot.AnalyzerInterface;

import analyzerBot.Types.FilterType;
import analyzerBot.Types.TypeMessage;

import java.util.Map;

public interface Analyzer<T> {

    boolean check(T content);

    TypeMessage getContentType();

    FilterType getFilterType();

    static <T> FilterType analyze(T message, Map<FilterType, Analyzer<T>> rules) {

        for (Map.Entry<FilterType, Analyzer<T>> analyzer : rules.entrySet()) {
            if (analyzer.getValue().check(message)) {
                return analyzer.getValue().getFilterType();
            }
        }
        return FilterType.GOOD;

//        for (Analyzer<T> analyzer : rules) {
//            if (analyzer.check(message)) {
//                return analyzer.getFilterType();
//            }
//        }
//        return FilterType.GOOD;
    }
}
