package analyzerBot.AnalyzerInterface;

import analyzerBot.Types.FilterType;

import java.util.Map;

public interface Analyzer<T> {

    boolean check(T content);

//    TypeMessage getContentType();

    FilterType getFilterType();

    static <T> FilterType analyze(T message, Map<FilterType, ControlRules<T>> rules) {

        for (Map.Entry<FilterType, ControlRules<T>> analyzer : rules.entrySet()) {
            if (analyzer.getValue().check(message) && analyzer.getValue().checkActivateRule()) {
                return analyzer.getValue().getFilterType();
            }
        }
        return FilterType.GOOD;
    }
}
