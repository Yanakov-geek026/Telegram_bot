package ru.gasu.yanakov.bot.analyzer.controllers.interfaces;

import ru.gasu.yanakov.bot.analyzer.publices.types.FilterType;

import java.util.Map;

public interface Analyzer<T> {

    boolean check(T content);

//    TypeMessage getContentType();

    FilterType getFilterType();

    static <T> FilterType analyze(T message, Map<String, ControlRules<T>> rules) {

        for (Map.Entry<String, ControlRules<T>> analyzer : rules.entrySet()) {
            if (analyzer.getValue().check(message) && analyzer.getValue().checkActivateRule()) {
                return analyzer.getValue().getFilterType();
            }
        }
        return FilterType.GOOD;
    }
}
