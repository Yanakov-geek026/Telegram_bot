package ru.gasu.yanakov.bot.analyzer.models;

import ru.gasu.yanakov.bot.analyzer.controllers.interfaces.ControlRules;
import ru.gasu.yanakov.bot.analyzer.controllers.check.text.*;
import org.telegram.abilitybots.api.db.DBContext;
import ru.gasu.yanakov.bot.analyzer.publices.types.FilterType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DBRuleText implements DBManager<String> {

    private Map<Long, Map<String, ControlRules<String>>> rulesChat;
    private static DBRuleText dbRuleText;

    public static DBRuleText getDbRuleText(DBContext db) {
        if (dbRuleText == null) {
            dbRuleText = new DBRuleText(db);
        }
        return dbRuleText;
    }

    private DBRuleText(DBContext db) {
        rulesChat = db.getMap("ChatRulesNew");
    }

    public String ChangeLongText(long chatId, int newTextSize) {
        Map<String, ControlRules<String>> listRuleText = rulesChat.get(chatId);
        listRuleText.put(DBManager.getIDRuleFromDB(listRuleText, FilterType.LONG_TEXT), new CheckLongText(newTextSize));
        rulesChat.put(chatId, listRuleText);
        return "Text length has been changed to " + newTextSize + " letters";
    }

    @Override
    public void setDBRule(long chatId) {
        if (rulesChat.isEmpty() || !rulesChat.containsKey(chatId)) {
            rulesChat.put(chatId, createRulesText());
        }
    }

    @Override
    public Map<String, ControlRules<String>> getRules(long chatId) {
        return rulesChat.get(chatId);
    }

    // Инициализация стандартных текстовых правил для чата
    private Map<String, ControlRules<String>> createRulesText() {
        Map<String, ControlRules<String>> rules = new HashMap<>();
        rules.put(UUID.randomUUID().toString(), new CheckLongText(20));
        rules.put(UUID.randomUUID().toString(), new CheckPhoneNumberText());
        rules.put(UUID.randomUUID().toString(), new CheckLinkText());

        return rules;
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
                    return "Rule enabled";
                } else {
                    rules.getValue().offRule();
                    rulesChat.put(chatId, listRuleText);
                    return "Rule disabled";
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
