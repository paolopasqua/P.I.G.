import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RuleScheduler {

    public HashMap<UUID, Rule> rulesList;

    public RuleScheduler () {

        rulesList = new HashMap<>();

    }

    public void addRule (ArrayList<String> parsedMessage) {

        Rule newRule = new Rule(parsedMessage);
        rulesList.put(newRule.getIdRegola(), newRule);

    }

    public void removeRule (UUID idRegola) {

        rulesList.get(idRegola).stopRule();
        rulesList.remove(idRegola);

    }
}
