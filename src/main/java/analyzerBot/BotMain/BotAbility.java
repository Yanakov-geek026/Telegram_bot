package analyzerBot.BotMain;

import analyzerBot.AnalyzerInterface.Analyzer;
import analyzerBot.Rule.Rules;
import analyzerBot.Rule.RulesManual;
import analyzerBot.Types.FilterType;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Map;

public class BotAbility implements AbilityExtension {

    private SilentSender silentSender;
    private DBContext db;
    private User user;

    BotAbility(SilentSender silentSender, DBContext db) {
        this.silentSender = silentSender;
        this.db = db;
    }

    // Вывод всех текущих правил в чате
    public Ability regulations() {
        return Ability
                .builder()
                .name("regulations")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(0)
                .action(ctx -> {
                    String regulations = "";
                    RulesManual rulesManual = new RulesManual(db, ctx.chatId());
                    for (Map.Entry<FilterType, String> manual : rulesManual.getManualRules().entrySet()) {
                        regulations += manual.getValue() + " (" + manual.getKey() + ") \n";
                    }
                    silentSender.send(regulations, ctx.chatId());
                })
                .build();
    }

    // Добавление запрещенного слова
    public Ability addRulesForbiddenWord() {
        return Ability
                .builder()
                .name("addforbiddenword")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(1)
                .action(ctx -> {
                    RulesManual rulesManual = new RulesManual(db, ctx.chatId());
                    Rules rules = new Rules(db, ctx.chatId());
                    rulesManual.addRulesForbiddenWord(rules, ctx.firstArg());
                    silentSender.send("Rule added", ctx.chatId());
                })
                .build();
    }

    // Отключение выбранного правила
    public Ability offRule() {
        return Ability
                .builder()
                .name("offrule")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(1)
                .action(ctx -> {
                    RulesManual rulesManual = new RulesManual(db, ctx.chatId());
                    Rules rules = new Rules(db, ctx.chatId());
//                    rulesManual.offRule(rules, Integer.parseInt(ctx.firstArg()));
                    rulesManual.switchRule(rules, Integer.parseInt(ctx.firstArg()), "(OFF)");
                    silentSender.send("Rule disabled", ctx.chatId());
                })
                .build();
    }

    public Ability onRule() {
        return Ability
                .builder()
                .name("onrule")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(1)
                .action(ctx -> {
                    RulesManual rulesManual = new RulesManual(db, ctx.chatId());
                    Rules rules = new Rules(db, ctx.chatId());
//                    rulesManual.onRule(rules, Integer.parseInt(ctx.firstArg()));
                    rulesManual.switchRule(rules, Integer.parseInt(ctx.firstArg()), "(ON)");
                    silentSender.send("Rule enabled ", ctx.chatId());
                })
                .build();
    }

    // Удалить все правила
    public Ability clearRules() {
        return Ability
                .builder()
                .name("clear")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(0)
                .action(ctx -> {
                    RulesManual rulesManual = new RulesManual(db, ctx.chatId());
                    Rules rules = new Rules(db, ctx.chatId());
                    rules.remove();
                    rulesManual.remove();
                    silentSender.send("Rules cleared", ctx.chatId());
                })
                .build();
    }

    // Анализ текстовых сообщений на нарушения правил
    public Reply message() {
        return Reply.of(update -> {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            FilterType analyzerMessage = analyzerMassage(messageText, chatId);

//            analyzerBot.Types.FilterType analyzerMessage = analyzerBot.Types.FilterType.GOOD;
//            analyzerBot.Rule.Rules rules = new analyzerBot.Rule.Rules(db, chatId);
//            rules.remove();
//            silentSender.send(rules.getSizeRules(), chatId);

            if (analyzerMessage != FilterType.GOOD) {
                int messageId = update.getMessage().getMessageId();

                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(chatId);
                deleteMessage.setMessageId(messageId);

                silentSender.execute(deleteMessage);
                silentSender.send("Message was deleted due to non-compliance. Cause: " + analyzerMessage,
                        update.getMessage().getChatId());
            }
        }, Flag.TEXT, update -> !update.getMessage().getText().contains("/"));
    }

    // Анализ фото на нарушения правил
    public Reply photo() {
        return Reply.of(update -> {
            silentSender.send("Photo", update.getMessage().getChatId());

        }, Flag.PHOTO);
    }

    private FilterType analyzerMassage(String messageText, long chatId) {
        Rules rules = new Rules(db, chatId);
        return Analyzer.analyze(messageText, rules.getRules());
    }
}
