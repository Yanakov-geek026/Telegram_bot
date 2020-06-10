package ru.gasu.yanakov.bot.analyzer.views;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import ru.gasu.yanakov.bot.analyzer.models.DataBaseManager;

public class RulesAbility implements AbilityExtension {

    private SilentSender silentSender;
    private DataBaseManager dataBaseManager;

    public RulesAbility(SilentSender silentSender, DBContext db) {
        this.silentSender = silentSender;
        dataBaseManager = new DataBaseManager(db);
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
                    dataBaseManager.setDBRule(ctx.chatId());
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
                    silentSender.send(dataBaseManager.getManualAllRules(ctx.chatId()), ctx.chatId());
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
                    dataBaseManager.addRulesCheckFindWordText(ctx.firstArg(), ctx.chatId());
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
                    silentSender.send(dataBaseManager.switchRule(Integer.parseInt(ctx.firstArg()),
                            false, ctx.chatId()), ctx.chatId());
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
                    silentSender.send(dataBaseManager.switchRule(Integer.parseInt(ctx.firstArg()),
                            true, ctx.chatId()), ctx.chatId());
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
                    dataBaseManager.remove(ctx.chatId());
                    silentSender.send("Rules cleared", ctx.chatId());
                })
                .build();
    }
}
