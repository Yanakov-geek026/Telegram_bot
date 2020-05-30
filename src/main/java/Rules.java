import org.telegram.abilitybots.api.db.DBContext;

import java.io.Serializable;
import java.util.*;

public class Rules {

    private Map<Long, List<Analyzer<String>>> rulesChat;
    private long chatId;

    Rules(DBContext db, long chatId) {

        rulesChat = db.getMap("ChatRules");
        this.chatId = chatId;

        if (rulesChat.isEmpty() || !rulesChat.containsKey(chatId)) {
            rulesChat.put(chatId, createRules());
        }
    }

        private List<Analyzer<String>> createRules() {
            List<Analyzer<String>> rules = new ArrayList<>();
            rules.add(new CheckLongText(20));
            rules.add(new CheckPhoneNumberText());
            rules.add(new CheckLinkText());
            rules.add(new CheckSmileText());
            return rules;
        }

        public List<Analyzer<String>> getRules () {
            return rulesChat.get(chatId);
        }

        public String getSizeRules() {
//            int size = rulesChat.size();
//            return Integer.toString(size);
            return String.valueOf(rulesChat.keySet());
        }

        public String remove(long chatId) {
            rulesChat.remove(chatId);
            return "remove";
        }
    }

