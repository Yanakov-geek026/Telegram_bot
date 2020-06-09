package analyzerBot.Rule;

import analyzerBot.AnalyzerInterface.ControlRules;
import analyzerBot.CheckPhoto.CheckHeightPhoto;
import analyzerBot.CheckPhoto.CheckWidthPhoto;
import analyzerBot.CheckText.*;
import analyzerBot.Types.FilterType;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.*;

public class Rules {

    private Map<Long, Map<String, ControlRules<String>>> rulesChat;
    private Map<Long, Map<String, ControlRules<List<PhotoSize>>>> rulesChatPhoto;
    private long chatId;

    public Rules(DBContext db, long chatId) {
        rulesChat = db.getMap("ChatRulesNew3");
        rulesChatPhoto = db.getMap("ChatRulesPhotoNew3");

        this.chatId = chatId;

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
        rules.put(UUID.randomUUID().toString(), new CheckSmileText());

        return rules;
    }

    // Инициализация стандартных правил на фото для чата
    private Map<String, ControlRules<List<PhotoSize>>> createRulesPhoto() {
        Map<String, ControlRules<List<PhotoSize>>> rules = new HashMap<>();
        rules.put(UUID.randomUUID().toString(), new CheckWidthPhoto(800));
        rules.put(UUID.randomUUID().toString(), new CheckHeightPhoto(100));

        return rules;
    }

    //Возвращаем список правил для текста
    public Map<String, ControlRules<String>> getRules() {
        return rulesChat.get(chatId);
    }

    //Возвращаем список правил для фото
    public Map<String, ControlRules<List<PhotoSize>>> getPhotoRules() {
        return rulesChatPhoto.get(chatId);
    }

    // Класс для проверки кол-во чатов в бд
    public String getSizeRules() {
        return String.valueOf(rulesChat.keySet());
    }

    // Удалить бд для определенного чата
    public void remove() {
        rulesChat.remove(chatId);
        rulesChatPhoto.remove(chatId);
    }

    // Добавление метода для анализа запрещенных слов
    public void addRulesCheckFindWordText(String word) {
        Map<String, ControlRules<String>> listRule = rulesChat.get(chatId);
        listRule.put(UUID.randomUUID().toString(), new CheckFindWordText(word));
        rulesChat.put(chatId, listRule);
    }

    // Выключение и выключение выбранных правил
    public String switchRule(int position, boolean status) {
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

    public String getManualAllRules() {
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

