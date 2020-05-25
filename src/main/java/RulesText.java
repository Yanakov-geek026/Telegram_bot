import java.util.ArrayList;
import java.util.List;

public class RulesText {

    private List<Analyzer<String>> rules = new ArrayList<>();

    public List<Analyzer<String>> createRules() {
        //List<Analyzer> rules = new ArrayList<>();
        rules.add(new CheckLongText(20));
        rules.add(new CheckPhoneNumberText());
        rules.add(new CheckLinkText());
        rules.add(new CheckSmileText());

        return rules;
    }
}
