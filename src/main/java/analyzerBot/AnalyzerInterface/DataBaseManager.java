package analyzerBot.AnalyzerInterface;

import analyzerBot.CheckPhoto.CheckHeightPhoto;
import analyzerBot.CheckPhoto.CheckWidthPhoto;
import analyzerBot.CheckText.*;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataBaseManager implements DBManager {

    private Map<Long, Map<String, ControlRules<String>>> rulesChat;
    private Map<Long, Map<String, ControlRules<List<PhotoSize>>>> rulesChatPhoto;

    public DataBaseManager(DBContext db) {
        DBRules(db);
    }

    private void DBRules(DBContext db) {
        rulesChat = db.getMap("ChatRulesNew1");
        rulesChatPhoto = db.getMap("ChatRulesPhotoNew1");
    }

    @Override
    public void setDBRule(long chatId) {
        if (rulesChat.isEmpty() || !rulesChat.containsKey(chatId)) {
            rulesChat.put(chatId, createRulesText());
            rulesChatPhoto.put(chatId, createRulesPhoto());
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

    // Инициализация стандартных правил на фото для чата
    private Map<String, ControlRules<List<PhotoSize>>> createRulesPhoto() {
        Map<String, ControlRules<List<PhotoSize>>> rules = new HashMap<>();
        rules.put(UUID.randomUUID().toString(), new CheckWidthPhoto(800));
        rules.put(UUID.randomUUID().toString(), new CheckHeightPhoto(100));

        return rules;
    }

    @Override
    public Map<String, ControlRules<String>> getRules(long chatId) {
        return rulesChat.get(chatId);
    }

    @Override
    public Map<String, ControlRules<List<PhotoSize>>> getPhotoRules(long chatId) {
        return rulesChatPhoto.get(chatId);
    }

    @Override
    public void remove(long chatId) {
        rulesChat.remove(chatId);
        rulesChatPhoto.remove(chatId);
    }

    @Override
    public void addRulesCheckFindWordText(String word, long chatId) {
        Map<String, ControlRules<String>> listRule = rulesChat.get(chatId);
        listRule.put(UUID.randomUUID().toString(), new CheckFindWordText(word));
        rulesChat.put(chatId, listRule);
    }

    @Override
    public String switchRule(int position, boolean status, long chatId) {
        Map<String, ControlRules<String>> listRuleText = rulesChat.get(chatId);
        Map<String, ControlRules<List<PhotoSize>>> listRulePhoto = rulesChatPhoto.get(chatId);
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
        for (Map.Entry<String, ControlRules<List<PhotoSize>>> rules : listRulePhoto.entrySet()) {
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
        for (Map.Entry<String, ControlRules<String>> rules : rulesChat.get(chatId).entrySet()) {
            manual.append(rules.getValue().manualRules());
        }
        for (Map.Entry<String, ControlRules<List<PhotoSize>>> rules : rulesChatPhoto.get(chatId).entrySet()) {
            manual.append(rules.getValue().manualRules());
        }
        return manual.toString();
    }
}
