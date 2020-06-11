package ru.gasu.yanakov.bot.analyzer.models;

import ru.gasu.yanakov.bot.analyzer.controllers.check.photo.CheckSizePhoto;
import ru.gasu.yanakov.bot.analyzer.controllers.interfaces.ControlRules;
import ru.gasu.yanakov.bot.analyzer.controllers.check.text.*;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import ru.gasu.yanakov.bot.analyzer.publices.types.FilterType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DBRuleText implements DBManager {

    private Map<Long, Map<String, ControlRules<String>>> rulesChat;

    public DBRuleText(DBContext db) {
        DBRules(db);
    }

    private void DBRules(DBContext db) {
        rulesChat = db.getMap("ChatRulesNew");
    }

    public String ChangeLongText(long chatId, int newTextSize) {
        Map<String, ControlRules<String>> listRuleText = rulesChat.get(chatId);
        for (Map.Entry<String, ControlRules<String>> rules : listRuleText.entrySet()) {
            if (rules.getValue().getFilterType() == FilterType.LONG_TEXT) {
                listRuleText.put(rules.getKey(), new CheckLongText(newTextSize));
                rulesChat.put(chatId, listRuleText);
                return "Text length has been changed to " + newTextSize + " letters";
            }
        }
        return "Rule not found";
    }

    @Override
    public void setDBRule(long chatId) {
        if (rulesChat.isEmpty() || !rulesChat.containsKey(chatId)) {
            rulesChat.put(chatId, createRulesText());
        }
    }

    // Инициализация стандартных текстовых правил для чата
    private Map<String, ControlRules<String>> createRulesText() {
        Map<String, ControlRules<String>> rules = new HashMap<>();
        rules.put(UUID.randomUUID().toString(), new CheckLongText(20));
        rules.put(UUID.randomUUID().toString(), new CheckPhoneNumberText());
        rules.put(UUID.randomUUID().toString(), new CheckLinkText());

        return rules;
    }

    public Map<String, ControlRules<String>> getRules(long chatId) {
        return rulesChat.get(chatId);
    }

    @Override
    public void remove(long chatId) {
        rulesChat.remove(chatId);
    }

    public void addRulesCheckFindWordText(String word, long chatId) {
        Map<String, ControlRules<String>> listRule = rulesChat.get(chatId);
        listRule.put(UUID.randomUUID().toString(), new CheckFindWordText(word));
        rulesChat.put(chatId, listRule);
    }

    @Override
    public String switchRule(int position, boolean status, long chatId) {
        Map<String, ControlRules<String>> listRuleText = rulesChat.get(chatId);
        int i = 1;

        for (Map.Entry<String, ControlRules<String>> rules : listRuleText.entrySet()) {
            if (i == position) {
                if (status) {
                    rules.getValue().onRule();
                    rulesChat.put(chatId, listRuleText);
                    return "Rile enabled";
                } else {
                    rules.getValue().offRule();
                    rulesChat.put(chatId, listRuleText);
                    return "Rile disabled";
                }
            }
            i++;
        }
        return "Rule not find";
    }

    @Override
    public String getManualAllRules(long chatId) {
        StringBuilder manual = new StringBuilder();
        int i = 1;
        for (Map.Entry<String, ControlRules<String>> rules : rulesChat.get(chatId).entrySet()) {
            manual.append(i).append(". ").append(rules.getValue().manualRules());
            i++;
        }
        return manual.toString();
    }
}
