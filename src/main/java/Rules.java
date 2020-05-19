import org.telegram.abilitybots.api.db.DBContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Rules implements AnalyzerText {

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

    public FilterType analyzerMessage(String messageText) {
        return new FilterManager(rulesChat.get(chatId)).analyzer(messageText);
    }

    @Override
    public AnalyzerText smileCheckTextAnalyzer() {
        return new CheckSmileText();
    }

    @Override
    public AnalyzerText longCheckTextAnalyzer(int textSize) {
        return new CheckLongText(textSize);
    }

    @Override
    public AnalyzerText phoneNumberCheckTextAnalyzer() {
        return new CheckPhoneNumberText();
    }

    @Override
    public AnalyzerText linkCheckTextAnalyzer() {
        return new CheckLinkText();
    }

    @Override
    public FilterType TextAnalyzer(String text) {
        return null;
    }

    @Override
    public boolean check(Object content) {
        return false;
    }

    @Override
    public TypeMessage getContentType() {
        return null;
    }

    @Override
    public FilterType getFilterType() {
        return null;
    }
}
