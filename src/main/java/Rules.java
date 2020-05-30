import org.telegram.abilitybots.api.db.DBContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//--------------------------------Временный класс------------------------------
public class Rules {

    private Map<Long, List<Analyzer>> rulesChat;
    private Analyzer rulesAnalyzer;
    private long chatId;


    public Rules(DBContext db, long chatId) {
        rulesChat = db.getMap("rulesChat");
        this.chatId = chatId;

        if (!rulesChat.containsKey(chatId)) {
            rulesChat.put(chatId, createRules());
        }
        //rulesChat.put(chatId, createRules());
    }

        private List<Analyzer> createRules() {
            List<Analyzer> rules = new ArrayList<>();

//        rules.add(Analyzer.linkCheckTextAnalyzer());
//        rules.add(Analyzer.longCheckTextAnalyzer(20));
//        rules.add(Analyzer.phoneNumberCheckTextAnalyzer());
//        rules.add(Analyzer.smileCheckTextAnalyzer());

            return rules;
        }

//        public FilterType analyzerMessage(String messageText) {
//        return new FilterManager(rulesChat.get(chatId)).analyzer(messageText);
    }

