package analyzerBot.Rule;

import analyzerBot.AnalyzerInterface.Analyzer;
import analyzerBot.CheckText.CheckFindWordText;
import analyzerBot.CheckText.CheckLongText;
import analyzerBot.CheckText.CheckPhoneNumberOrLink;
import analyzerBot.CheckText.CheckSmileText;
import org.telegram.abilitybots.api.db.DBContext;

import java.util.*;

public class Rules {

    private Map<Long, List<Analyzer<String>>> rulesChat;
    private long chatId;

    public Rules(DBContext db, long chatId) {

        rulesChat = db.getMap("ChatRules3");
        this.chatId = chatId;

        if (rulesChat.isEmpty() || !rulesChat.containsKey(chatId)) {
            rulesChat.put(chatId, createRules());
        }
    }

        private List<Analyzer<String>> createRules() {
            List<Analyzer<String>> rules = new ArrayList<>();
            rules.add(new CheckLongText(20));
            rules.add(new CheckPhoneNumberOrLink());
            rules.add(new CheckSmileText());

            return rules;
        }

        public List<Analyzer<String>> getRules () {
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

        public void addRulesCheckFindWordText(String word) {
            List<Analyzer<String>> listRule = rulesChat.get(chatId);
            remove();
            listRule.add(new CheckFindWordText(word));
            rulesChat.put(chatId, listRule);
            //rulesChat.get(chatId).add(new CheckFindWordText(word));
        }
    }

