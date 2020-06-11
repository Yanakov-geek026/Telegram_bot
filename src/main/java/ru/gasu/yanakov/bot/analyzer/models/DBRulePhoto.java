package ru.gasu.yanakov.bot.analyzer.models;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import ru.gasu.yanakov.bot.analyzer.controllers.check.photo.CheckFileSizePhoto;
import ru.gasu.yanakov.bot.analyzer.controllers.check.photo.CheckSizePhoto;
import ru.gasu.yanakov.bot.analyzer.controllers.interfaces.ControlRules;
import ru.gasu.yanakov.bot.analyzer.publices.types.FilterType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DBRulePhoto implements DBManager {

    private Map<Long, Map<String, ControlRules<List<PhotoSize>>>> rulesChatPhoto;

    public DBRulePhoto(DBContext db) {
        DBRules(db);
    }

    private void DBRules(DBContext db) {
        rulesChatPhoto = db.getMap("ChatRulesPhotoNew");
    }

    public String ChangeSizePhoto(long chatId, int width, int height) {
        Map<String, ControlRules<List<PhotoSize>>> listRulePhoto = rulesChatPhoto.get(chatId);
        for (Map.Entry<String, ControlRules<List<PhotoSize>>> rules : listRulePhoto.entrySet()) {
            if (rules.getValue().getFilterType() == FilterType.PHOTO_SIZE) {
                listRulePhoto.put(rules.getKey(), new CheckSizePhoto(width, height));
                rulesChatPhoto.put(chatId, listRulePhoto);
                return "Photo size has been changed to " + width + "x" + height + " letters";
            }
        }
        return "Rule not found";
    }

    public Map<String, ControlRules<List<PhotoSize>>> getPhotoRules(long chatId) {
        return rulesChatPhoto.get(chatId);
    }

    @Override
    public void remove(long chatId) {
        rulesChatPhoto.remove(chatId);
    }

    @Override
    public String switchRule(int position, boolean status, long chatId) {
        Map<String, ControlRules<List<PhotoSize>>> listRulePhoto = rulesChatPhoto.get(chatId);
        int i = 1;
        for (Map.Entry<String, ControlRules<List<PhotoSize>>> rules : listRulePhoto.entrySet()) {
            if (i == position) {
                if (status) {
                    rules.getValue().onRule();
                    rulesChatPhoto.put(chatId, listRulePhoto);
                    return "Rule enabled";
                } else {
                    rules.getValue().offRule();
                    rulesChatPhoto.put(chatId, listRulePhoto);
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
        for (Map.Entry<String, ControlRules<List<PhotoSize>>> rules : rulesChatPhoto.get(chatId).entrySet()) {
            manual.append(i).append(". ").append(rules.getValue().manualRules());
            i++;
        }
        return manual.toString();
    }

    @Override
    public void setDBRule(long chatId) {
        if (rulesChatPhoto.isEmpty() || !rulesChatPhoto.containsKey(chatId)) {
            rulesChatPhoto.put(chatId, createRulesPhoto());
        }
    }

    // Инициализация стандартных правил на фото для чата
    private Map<String, ControlRules<List<PhotoSize>>> createRulesPhoto() {
        Map<String, ControlRules<List<PhotoSize>>> rules = new HashMap<>();
        rules.put(UUID.randomUUID().toString(), new CheckSizePhoto(800, 100));
        rules.put(UUID.randomUUID().toString(), new CheckFileSizePhoto(1));

        return rules;
    }
}
