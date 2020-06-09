package analyzerBot.Rule;

import analyzerBot.AnalyzerInterface.ControlRules;
import analyzerBot.CheckPhoto.CheckHeightPhoto;
import analyzerBot.CheckPhoto.CheckWidthPhoto;
import analyzerBot.CheckText.*;
import analyzerBot.Types.FilterType;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rules {

    private Map<Long, Map<FilterType, ControlRules<String>>> rulesChat, allRulesText;
    private Map<Long, Map<FilterType, ControlRules<List<PhotoSize>>>> rulesChatPhoto, allRulesTextPhoto;
    private Map<Long, List<String>> forbiddenWords;
    private long chatId;

    public Rules(DBContext db, long chatId) {
//        forbiddenWords = db.getMap("ForbiddenWords");
//        rulesChat = db.getMap("ChatRules3");
//        allRulesText = db.getMap("AllRulesText");
//
//        rulesChatPhoto = db.getMap("ChatRulesPhoto3");
//        allRulesTextPhoto = db.getMap("AllRulesTextPhoto");

        forbiddenWords = db.getMap("ForbiddenWordsNew2");
        rulesChat = db.getMap("ChatRulesNew2");
        allRulesText = db.getMap("AllRulesTextNew2");

        rulesChatPhoto = db.getMap("ChatRulesPhotoNew2");
        allRulesTextPhoto = db.getMap("AllRulesTextPhotoNew2");

        this.chatId = chatId;

        if (rulesChat.isEmpty() || !rulesChat.containsKey(chatId)) {
            forbiddenWords.put(chatId, new ArrayList<>());
            rulesChat.put(chatId, createRulesText());
            allRulesText.put(chatId, createRulesText());

            rulesChatPhoto.put(chatId, createRulesPhoto());
            allRulesTextPhoto.put(chatId, createRulesPhoto());
        }
    }

    // Инициализация стандартных текстовых правил для чата
    private Map<FilterType, ControlRules<String>> createRulesText() {
        Map<FilterType, ControlRules<String>> rules = new HashMap<>();
        rules.put(FilterType.LONG_TEXT, new CheckLongText(20));
        rules.put(FilterType.PHONE_NUMBER, new CheckPhoneNumberText());
        rules.put(FilterType.REPOST_LINK, new CheckLinkText());
        rules.put(FilterType.USE_SMILE, new CheckSmileText());

        return rules;
    }

    // Инициализация стандартных правил на фото для чата
    private Map<FilterType, ControlRules<List<PhotoSize>>> createRulesPhoto() {
        Map<FilterType, ControlRules<List<PhotoSize>>> rules = new HashMap<>();
        rules.put(FilterType.PHOTO_VERY_WIDTH, new CheckWidthPhoto(800));
        rules.put(FilterType.PHOTO_VERY_HEIGHT, new CheckHeightPhoto(100));

        return rules;
    }

    //Возвращаем список правил для текста
    public Map<FilterType, ControlRules<String>> getRules() {
        return rulesChat.get(chatId);
    }

    //Возвращаем список правил для фото
    public Map<FilterType, ControlRules<List<PhotoSize>>> getPhotoRules() {
        return rulesChatPhoto.get(chatId);
    }

    // Класс для проверки кол-во чатов в бд
    public String getSizeRules() {
        return String.valueOf(rulesChat.keySet());
    }

    // Удалить бд для определенного чата
    public void remove() {
        rulesChat.remove(chatId);
        allRulesText.remove(chatId);
        forbiddenWords.remove(chatId);

        rulesChatPhoto.remove(chatId);
        allRulesTextPhoto.remove(chatId);
    }

    // Добавление метода для анализа запрещенных слов
    void addRulesCheckFindWordText(String word) {
        Map<FilterType, ControlRules<String>> listRule = rulesChat.get(chatId);
        List<String> listForbiddenWords = forbiddenWords.get(chatId);

        listForbiddenWords.add(word);
        forbiddenWords.put(chatId, listForbiddenWords);

        listRule.put(FilterType.FORBIDDEN_WORD, new CheckFindWordText(forbiddenWords.get(chatId)));

        rulesChat.put(chatId, listRule);
        allRulesText.put(chatId, listRule);
    }

    // Выключение выбранных правил
    void offRule(FilterType typeRule) {
        Map<FilterType, ControlRules<String>> listRuleText = rulesChat.get(chatId);
        Map<FilterType, ControlRules<List<PhotoSize>>> listRulePhoto = rulesChatPhoto.get(chatId);

        listRuleText.remove(typeRule);
        listRulePhoto.remove(typeRule);

        rulesChat.put(chatId, listRuleText);
        rulesChatPhoto.put(chatId, listRulePhoto);
    }

    // Включение выбранных правил
    void onRule(FilterType typeRule) {
        Map<FilterType, ControlRules<String>> listRule = rulesChat.get(chatId);
        Map<FilterType, ControlRules<List<PhotoSize>>> listRulePhoto = rulesChatPhoto.get(chatId);

        listRule.put(typeRule, allRulesText.get(chatId).get(typeRule));
        listRulePhoto.put(typeRule, allRulesTextPhoto.get(chatId).get(typeRule));

        rulesChat.put(chatId, listRule);
        rulesChatPhoto.put(chatId, listRulePhoto);
    }

    public String getManualAllRules() {
        StringBuilder manual = new StringBuilder();
        for (Map.Entry<FilterType, ControlRules<String>> rules : rulesChat.get(chatId).entrySet()) {
            manual.append(rules.getValue().manualRules());
        }
        for (Map.Entry<FilterType, ControlRules<List<PhotoSize>>> rules : rulesChatPhoto.get(chatId).entrySet()) {
            manual.append(rules.getValue().manualRules());
        }
        return manual.toString();
    }
}

