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

    private Map<Long, Map<FilterType, Analyzer<String>>> rulesChat;
    private long chatId;

    public Rules(DBContext db, long chatId) {

        rulesChat = db.getMap("ChatRules3");
        this.chatId = chatId;

        if (rulesChat.isEmpty() || !rulesChat.containsKey(chatId)) {
            rulesChat.put(chatId, createRules());
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

    public Map<FilterType, Analyzer<String>> getRules () {
        return rulesChat.get(chatId);
    }

    // Класс для проверки кол-во чатов в бд
    public String getSizeRules() {
        return String.valueOf(rulesChat.keySet());
    }

    // Удалить бд для определенного чата
    public void remove() {
        rulesChat.remove(chatId);
    }

    // Добавление метода для анализа запрещенных слов
    public void addRulesCheckFindWordText(String word) {
        Map<FilterType, Analyzer<String>> listRule = rulesChat.get(chatId);
        remove();
        listRule.put(FilterType.FORBIDDEN_WORD, new CheckFindWordText(word));
        rulesChat.put(chatId, listRule);
        //rulesChat.get(chatId).add(new CheckFindWordText(word));
    }

    public void offRule(FilterType typeRule) {
        Map<FilterType, Analyzer<String>> listRule = rulesChat.get(chatId);
        remove();
        listRule.remove(typeRule);
        rulesChat.put(chatId, listRule);
    }
}

