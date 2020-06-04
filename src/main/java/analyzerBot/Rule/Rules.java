package analyzerBot.Rule;

import analyzerBot.AnalyzerInterface.Analyzer;
import analyzerBot.CheckText.CheckFindWordText;
import analyzerBot.CheckText.CheckLongText;
import analyzerBot.CheckText.CheckPhoneNumberOrLink;
import analyzerBot.CheckText.CheckSmileText;
import analyzerBot.Types.FilterType;
import org.telegram.abilitybots.api.db.DBContext;

import java.util.*;

public class Rules {

    private Map<Long, Map<FilterType, Analyzer<String>>> rulesChat, allRulesText;
    private Map<Long, List<String>> forbiddenWords;
    private long chatId;

    public Rules(DBContext db, long chatId) {
        forbiddenWords = db.getMap("ForbiddenWords");
        rulesChat = db.getMap("ChatRules3");
        allRulesText = db.getMap("AllRulesText");
        this.chatId = chatId;

        if (rulesChat.isEmpty() || !rulesChat.containsKey(chatId)) {
            rulesChat.put(chatId, createRules());
            allRulesText.put(chatId, createRules());
            forbiddenWords.put(chatId, new ArrayList<>());
        }
    }

    // Инициализация стандартных правил для чата
    private Map<FilterType, Analyzer<String>> createRules() {
        Map<FilterType, Analyzer<String>> rules = new HashMap<>();
        rules.put(FilterType.LONG_TEXT, new CheckLongText(20));
        rules.put(FilterType.PHONE_NUMBER, new CheckPhoneNumberOrLink());
        rules.put(FilterType.USE_SMILE, new CheckSmileText());

        return rules;
    }

    public Map<FilterType, Analyzer<String>> getRules() {
        return rulesChat.get(chatId);
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
    }

    // Добавление метода для анализа запрещенных слов
    void addRulesCheckFindWordText(String word) {
        Map<FilterType, Analyzer<String>> listRule = rulesChat.get(chatId);
        List<String> listForbiddenWords = forbiddenWords.get(chatId);

        listForbiddenWords.add(word);
        forbiddenWords.put(chatId, listForbiddenWords);

        listRule.put(FilterType.FORBIDDEN_WORD, new CheckFindWordText(forbiddenWords.get(chatId)));

        rulesChat.put(chatId, listRule);
        allRulesText.put(chatId, listRule);
    }

    // Выключение выбранных правил
    void offRule(FilterType typeRule) {
        Map<FilterType, Analyzer<String>> listRule = rulesChat.get(chatId);
        listRule.remove(typeRule);

        rulesChat.put(chatId, listRule);
    }

    // Включение выбранных правил
    void onRule(FilterType typeRule) {
        Map<FilterType, Analyzer<String>> listRule = rulesChat.get(chatId);
        listRule.put(typeRule, allRulesText.get(chatId).get(typeRule));

        rulesChat.put(chatId, listRule);
    }
}

