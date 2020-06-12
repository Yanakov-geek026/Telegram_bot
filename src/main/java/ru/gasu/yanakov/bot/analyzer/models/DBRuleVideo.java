package ru.gasu.yanakov.bot.analyzer.models;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.meta.api.objects.Video;
import ru.gasu.yanakov.bot.analyzer.controllers.check.video.CheckDurationVideo;
import ru.gasu.yanakov.bot.analyzer.controllers.check.video.CheckSizeFileVideo;
import ru.gasu.yanakov.bot.analyzer.controllers.check.video.CheckSizeVideo;
import ru.gasu.yanakov.bot.analyzer.controllers.interfaces.ControlRules;
import ru.gasu.yanakov.bot.analyzer.publices.types.FilterType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DBRuleVideo implements DBManager<Video> {

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

    @Override
    public Map<String, ControlRules<Video>> getRules(long chatId) {
        return rulesChatVideo.get(chatId);
    }

    private Map<String, ControlRules<Video>> createRulesVideo() {
        Map<String, ControlRules<Video>> rules = new HashMap<>();
        rules.put(UUID.randomUUID().toString(), new CheckDurationVideo(10));
        rules.put(UUID.randomUUID().toString(), new CheckSizeFileVideo(1000));
        rules.put(UUID.randomUUID().toString(), new CheckSizeVideo(800, 600));

        return rules;
    }

    public String ChangeSizeFileVideo(long chatId, int fileSizePhoto) {
        Map<String, ControlRules<Video>> listRuleVideo = rulesChatVideo.get(chatId);
        listRuleVideo.put(DBManager.getIDRuleFromDB(listRuleVideo, FilterType.PHOTO_FILE_SIZE), new CheckSizeFileVideo(fileSizePhoto));
        rulesChatVideo.put(chatId, listRuleVideo);
        return "Video file size has been changed to " + fileSizePhoto + " mb";
    }

    public String ChangeSizeVideo(long chatId, int width, int height) {
        Map<String, ControlRules<Video>> listRuleVideo = rulesChatVideo.get(chatId);
        listRuleVideo.put(DBManager.getIDRuleFromDB(listRuleVideo, FilterType.PHOTO_SIZE), new CheckSizeVideo(width, height));
        rulesChatVideo.put(chatId, listRuleVideo);
        return "Video size has been changed to " + width + "x" + height + " pixels";
    }
}
