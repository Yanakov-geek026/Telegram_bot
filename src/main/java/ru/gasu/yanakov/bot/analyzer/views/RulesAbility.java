package ru.gasu.yanakov.bot.analyzer.views;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import ru.gasu.yanakov.bot.analyzer.models.DBRulePhoto;
import ru.gasu.yanakov.bot.analyzer.models.DBRuleText;

public class RulesAbility implements AbilityExtension {

    private SilentSender silentSender;
    private DBRuleText dbRuleText;
    private DBRulePhoto dbRulePhoto;

    public RulesAbility(SilentSender silentSender, DBContext db) {
        this.silentSender = silentSender;
        dbRuleText = new DBRuleText(db);
        dbRulePhoto = new DBRulePhoto(db);
    }

    // Инициализация правил
    public Ability start() {
        return Ability
                .builder()
                .name("start")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(0)
                .action(ctx -> {
                    dbRuleText.setDBRule(ctx.chatId());
                    dbRulePhoto.setDBRule(ctx.chatId());
                    silentSender.send("Rules created", ctx.chatId());
                })
                .build();
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
                    String manual;
                    manual = "text:" + "\n" +
                            dbRuleText.getManualAllRules(ctx.chatId()) + "\n" +
                            "photo:" + "\n" +
                            dbRulePhoto.getManualAllRules(ctx.chatId());
                    silentSender.send(manual, ctx.chatId());
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
                    dbRuleText.addRulesCheckFindWordText(ctx.firstArg(), ctx.chatId());
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
                .input(2)
                .action(ctx -> {
                    switch (ctx.firstArg()) {
                        case "text":
                            silentSender.send(dbRuleText.switchRule(Integer.parseInt(ctx.secondArg()),
                                    false, ctx.chatId()), ctx.chatId());
                            break;
                        case "photo":
                            silentSender.send(dbRulePhoto.switchRule(Integer.parseInt(ctx.secondArg()),
                                    false, ctx.chatId()), ctx.chatId());
                            break;
                        default:
                            silentSender.send("Not found rules", ctx.chatId());
                    }
                })
                .build();
    }

    public Ability onRule() {
        return Ability
                .builder()
                .name("onrule")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(2)
                .action(ctx -> {
                    switch (ctx.firstArg()) {
                        case "text":
                            silentSender.send(dbRuleText.switchRule(Integer.parseInt(ctx.secondArg()),
                                    true, ctx.chatId()), ctx.chatId());
                            break;
                        case "photo":
                            silentSender.send(dbRulePhoto.switchRule(Integer.parseInt(ctx.secondArg()),
                                    true, ctx.chatId()), ctx.chatId());
                            break;
                        default:
                            silentSender.send("Not found rules", ctx.chatId());
                    }
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
                    dbRuleText.remove(ctx.chatId());
                    dbRulePhoto.remove(ctx.chatId());
                    silentSender.send("Rules cleared", ctx.chatId());
                })
                .build();
    }

    public Ability changeRuleLongText() {
        return Ability
                .builder()
                .name("changelongtext")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(1)
                .action(ctx -> {
                    silentSender.send(dbRuleText.ChangeLongText(ctx.chatId(), Integer.parseInt(ctx.firstArg())),
                            ctx.chatId());
                })
                .build();
    }
}
