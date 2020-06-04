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

    // Инициализация стандартных правил для чата
    private Map<FilterType, String> createManual() {
        Map<FilterType, String> manual = new HashMap<>();
        manual.put(FilterType.LONG_TEXT, "(ON) Cannot send message more than 20 characters");
        manual.put(FilterType.USE_SMILE, "(ON) You can not use emoticons");
        manual.put(FilterType.PHONE_NUMBER, "(ON) Cannot send phone number");
        manual.put(FilterType.REPOST_LINK, "(ON) Do not send various links");

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

    // Добавление запрещенного слова
    public void addRulesForbiddenWord(Rules rules, String description) {
        Map<FilterType, String> manual = manualRules.get(chatId);
        if (!manual.containsKey(FilterType.FORBIDDEN_WORD)) {
            manual.put(FilterType.FORBIDDEN_WORD, "(ON) You can't use word a ( " + description + " ) ");
        } else {
            String valueManual = manual.get(FilterType.FORBIDDEN_WORD) + ", ( " + description + " ) ";
            manual.put(FilterType.FORBIDDEN_WORD, valueManual);
        }
        manualRules.put(chatId, manual);
        rules.addRulesCheckFindWordText(description);
    }

    // Включение и выключение выбранных правил
    public void switchRule(Rules rules, int positionRule, String attribute) {
        Map<FilterType, String> manualRuleText = manualRules.get(chatId);
        String disAttributeText = (attribute.equals("(ON)") ? "(OFF)" : "(ON)");
        int indentation = (attribute.equals("(ON)") ? 5 : 4);
        int i = 0;
        for (Map.Entry<FilterType, String> manual : manualRuleText.entrySet()) {
            i++;
            if (i == positionRule && manual.getValue().contains(disAttributeText)) {
                String textManual = manual.getValue();
                manualRuleText.put(manual.getKey(), attribute + textManual.substring(indentation));
                manualRules.put(chatId, manualRuleText);
                if (attribute.equals("(ON)")) {
                    rules.onRule(manual.getKey());
                } else {
                    rules.offRule(manual.getKey());
                }
            }
        }
    }
}
