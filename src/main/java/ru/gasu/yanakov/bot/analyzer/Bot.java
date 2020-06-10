package ru.gasu.yanakov.bot.analyzer;

import ru.gasu.yanakov.bot.analyzer.views.MessageAbility;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import ru.gasu.yanakov.bot.analyzer.views.RulesAbility;

public class Bot extends AbilityBot {

    private static String BOT_USERNAME = "Chat_Analyzer_bot";
    private static String BOT_TOKEN = System.getenv("TOKEN");

    Bot(DefaultBotOptions botOptions) {
        super(BOT_TOKEN, BOT_USERNAME, botOptions);
    }

    public AbilityExtension ability() {
        return new MessageAbility(silent, db);
    }

    public AbilityExtension abilityT() {
        return new RulesAbility(silent, db);
    }

    @Override
    public int creatorId() {
        return 0;
    }
}
