package ru.gasu.yanakov.bot.analyzer.views;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import ru.gasu.yanakov.bot.analyzer.models.DBRulePhoto;
import ru.gasu.yanakov.bot.analyzer.models.DBRuleText;
import ru.gasu.yanakov.bot.analyzer.models.DBRuleVideo;

public class RulesAbility implements AbilityExtension {

    private SilentSender silentSender;
    private DBRuleText dbRuleText;
    private DBRulePhoto dbRulePhoto;
    private DBRuleVideo dbRuleVideo;

    public RulesAbility(SilentSender silentSender, DBContext db) {
        this.silentSender = silentSender;
        dbRuleText = DBRuleText.getDbRuleText(db);
        dbRulePhoto = DBRulePhoto.getDbRulePhoto(db);
        dbRuleVideo = DBRuleVideo.getDbRuleVideo(db);
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
                    dbRuleVideo.setDBRule(ctx.chatId());
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
                            dbRulePhoto.getManualAllRules(ctx.chatId()) + "\n" +
                            "video:" + "\n" +
                            dbRuleVideo.getManualAllRules(ctx.chatId());
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
                        case "video":
                            silentSender.send(dbRuleVideo.switchRule(Integer.parseInt(ctx.secondArg()),
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
                        case "video":
                            silentSender.send(dbRuleVideo.switchRule(Integer.parseInt(ctx.secondArg()),
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
                    dbRuleVideo.remove(ctx.chatId());
                    silentSender.send("Rules cleared", ctx.chatId());
                })
                .build();
    }

    // Изменение правила на максимальный размер текста в сообщение
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

    // Изменение правил на габариты отправляемых фото
    public Ability changeRuleSizePhoto() {
        return Ability
                .builder()
                .name("changesizephoto")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(2)
                .action(ctx -> {
                    silentSender.send(dbRulePhoto.ChangeSizePhoto(ctx.chatId(),
                            Integer.parseInt(ctx.firstArg()),
                            Integer.parseInt(ctx.secondArg())),
                            ctx.chatId());
                })
                .build();
    }

    // Изменение правил на размер отправляемых фото
    public Ability changeRuleSizeFilePhoto() {
        return Ability
                .builder()
                .name("changesizefilephoto")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(1)
                .action(ctx -> {
                    silentSender.send(dbRulePhoto.ChangeSizeFilePhoto(ctx.chatId(), Integer.parseInt(ctx.firstArg())),
                            ctx.chatId());
                })
                .build();
    }

    // Изменение правил на габариты отправляемых видео
    public Ability changeRuleSizeVideo() {
        return Ability
                .builder()
                .name("changesizevideo")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(2)
                .action(ctx -> {
                    silentSender.send(dbRuleVideo.ChangeSizeVideo(ctx.chatId(),
                            Integer.parseInt(ctx.firstArg()),
                            Integer.parseInt(ctx.secondArg())),
                            ctx.chatId());
                })
                .build();
    }

    // Изменение правил на размер отправляемых видео
    public Ability changeRuleSizeFileVideo() {
        return Ability
                .builder()
                .name("changesizefilevideo")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(1)
                .action(ctx -> {
                    silentSender.send(dbRuleVideo.ChangeSizeFileVideo(ctx.chatId(), Integer.parseInt(ctx.firstArg())),
                            ctx.chatId());
                })
                .build();
    }
}
