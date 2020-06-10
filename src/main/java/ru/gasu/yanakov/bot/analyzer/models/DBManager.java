package ru.gasu.yanakov.bot.analyzer.models;

import ru.gasu.yanakov.bot.analyzer.controllers.interfaces.ControlRules;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;
import java.util.Map;

public interface DBManager {

    Map<String, ControlRules<String>> getRules(long chatId);

    Map<String, ControlRules<List<PhotoSize>>> getPhotoRules(long chatId);

    void remove(long chatId);

    void addRulesCheckFindWordText(String word, long chatId);

    String switchRule(int position, boolean status, long chatId);

    String getManualAllRules(long chatId);

    void setDBRule(long chatId);
}
