package ru.gasu.yanakov.bot.analyzer.models;

import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import ru.gasu.yanakov.bot.analyzer.controllers.interfaces.ControlRules;
import ru.gasu.yanakov.bot.analyzer.publices.types.FilterType;

import java.util.List;
import java.util.Map;

public interface DBManager<T> {

    void remove(long chatId);

    String switchRule(int position, boolean status, long chatId);

    String getManualAllRules(long chatId);

    void setDBRule(long chatId);

    Map<String, ControlRules<T>> getRules(long chatId);

    static <T> String getIDRuleFromDB(Map<String, ControlRules<T>>  rules, FilterType filterType) {
        for (Map.Entry<String, ControlRules<T>> rule : rules.entrySet() ) {
            if (rule.getValue().getFilterType() == filterType) {
                return rule.getKey();
            }
        }
        return null;
    }
}
