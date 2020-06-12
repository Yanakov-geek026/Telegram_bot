package ru.gasu.yanakov.bot.analyzer.models;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.meta.api.objects.Video;
import ru.gasu.yanakov.bot.analyzer.controllers.check.video.CheckDurationVideo;
import ru.gasu.yanakov.bot.analyzer.controllers.interfaces.ControlRules;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DBRuleVideo implements DBManager {

    private Map<Long, Map<String, ControlRules<Video>>> rulesChatVideo;
    private static DBRuleVideo dbRuleVideo;

    public static DBRuleVideo getDbRuleVideo (DBContext db) {
        if (dbRuleVideo == null) {
            dbRuleVideo = new DBRuleVideo(db);
        }
        return dbRuleVideo;
    }

    private DBRuleVideo(DBContext db) {
        rulesChatVideo = db.getMap("ChatRulesVideoNew");
    }

    public Map<String, ControlRules<Video>> getVideoRules(long chatId) {
        return rulesChatVideo.get(chatId);
    }

    @Override
    public void remove(long chatId) {
        rulesChatVideo.remove(chatId);
    }

    @Override
    public String switchRule(int position, boolean status, long chatId) {
        Map<String, ControlRules<Video>> listRuleText = rulesChatVideo.get(chatId);
        int i = 1;

        for (Map.Entry<String, ControlRules<Video>> rules : listRuleText.entrySet()) {
            if (i == position) {
                if (status) {
                    rules.getValue().onRule();
                    rulesChatVideo.put(chatId, listRuleText);
                    return "Rule enabled";
                } else {
                    rules.getValue().offRule();
                    rulesChatVideo.put(chatId, listRuleText);
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
        for (Map.Entry<String, ControlRules<Video>> rules : rulesChatVideo.get(chatId).entrySet()) {
            manual.append(i).append(". ").append(rules.getValue().manualRules());
            i++;
        }
        return manual.toString();
    }

    @Override
    public void setDBRule(long chatId) {
        if (rulesChatVideo.isEmpty() || !rulesChatVideo.containsKey(chatId)) {
            rulesChatVideo.put(chatId, createRulesVideo());
        }
    }

    private Map<String, ControlRules<Video>> createRulesVideo() {
        Map<String, ControlRules<Video>> rules = new HashMap<>();
        rules.put(UUID.randomUUID().toString(), new CheckDurationVideo(10));

        return rules;
    }
}
