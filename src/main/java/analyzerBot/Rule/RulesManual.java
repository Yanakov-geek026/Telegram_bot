package analyzerBot.Rule;

import analyzerBot.Types.FilterType;
import org.telegram.abilitybots.api.db.DBContext;

import java.util.HashMap;
import java.util.Map;

public class RulesManual {

    private Map<Long, Map<FilterType, String>> manualRules;
    private long chatId;

    public RulesManual(DBContext db, long chatId) {
        this.chatId = chatId;
        manualRules = db.getMap("manualRules1");

        if (manualRules.isEmpty() || !manualRules.containsKey(chatId)) {
            manualRules.put(chatId, createManual());
        }
    }

    private Map<FilterType, String> createManual() {
        Map<FilterType, String> manual = new HashMap<>();
        manual.put(FilterType.LONG_TEXT, "Cannot send message more than 20 characters");
        manual.put(FilterType.USE_SMILE, "You can not use emoticons");
        manual.put(FilterType.PHONE_NUMBER, "Cannot send phone number");
        manual.put(FilterType.REPOST_LINK, "Do not send various links");

        return manual;
    }

    public Map<FilterType, String> getManualRules() {
        return manualRules.get(chatId);
    }

    // Удалить бд для определенного чата
    public void remove() {
        manualRules.remove(chatId);
    }

    // Класс для проверки кол-во чатов в бд
    public String getSizeRules() {
        return String.valueOf(manualRules.keySet());
    }

    public void addRules(Rules rules, String description) {
        Map<FilterType, String> manual = manualRules.get(chatId);
        if (!manual.containsKey(FilterType.FORBIDDEN_WORD)) {
            manual.put(FilterType.FORBIDDEN_WORD, "You can't use word a ( " + description + " ) ");
        } else {
            String valueManual = manual.get(FilterType.FORBIDDEN_WORD) + ", ( " + description + " ) " ;
            manual.put(FilterType.FORBIDDEN_WORD, valueManual);
        }
        remove();
        manualRules.put(chatId, manual);
        rules.addRulesCheckFindWordText(description);
    }
}
