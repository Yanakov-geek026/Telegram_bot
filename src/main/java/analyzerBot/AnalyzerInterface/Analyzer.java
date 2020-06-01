package analyzerBot.AnalyzerInterface;

import analyzerBot.Types.FilterType;
import analyzerBot.Types.TypeMessage;

import java.util.List;

public interface Analyzer<T> {

    boolean check(T content);

    TypeMessage getContentType();

    FilterType getFilterType();

    static <T> FilterType analyze(T message, List<Analyzer<T>> rules) {

        for (Analyzer<T> analyzer : rules) {
            if (analyzer.check(message)) {
                return analyzer.getFilterType();
            }
        }
        return FilterType.GOOD;
    }
}
