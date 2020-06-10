package analyzerBot.BotMain;

import analyzerBot.AnalyzerInterface.Analyzer;
import analyzerBot.AnalyzerInterface.DataBaseManager;
import analyzerBot.Types.FilterType;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class BotAbility implements AbilityExtension {

    private SilentSender silentSender;
    private DataBaseManager dataBaseManager;

    BotAbility(SilentSender silentSender, DBContext db) {
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

    // Анализ текстовых сообщений на нарушения правил
    public Reply message() {
        return Reply.of( update -> {
                    String messageText = update.getMessage().getText();
                    FilterType analyzerMessage = analyzerMassage(messageText, update.getMessage().getChatId());
                    deleteMessage(update, analyzerMessage);

                }, Flag.TEXT,
                update -> !update.getMessage().getText().contains("/"));
    }

    // Анализ ссылки на нарушения правил
    public Reply messageLink() {
        return Reply.of(update -> {
                    String messageText = update.getMessage().getText();
                    FilterType analyzerMessage = analyzerMassage(messageText, update.getMessage().getChatId());
                    deleteMessage(update, analyzerMessage);
                }, Flag.MESSAGE,
                update -> !update.getMessage().hasPhoto() && update.getMessage().getText().contains("https"));
    }

    // Анализ фото на нарушения правил
    public Reply photo() {
        return Reply.of(update -> {
            List<PhotoSize> photo = update.getMessage().getPhoto();
            FilterType analyzerPhoto = analyzerPhoto(photo, update.getMessage().getChatId());
            deleteMessage(update, analyzerPhoto);
        }, Flag.PHOTO);
    }

    // Метод по удаления сообщения
    private void deleteMessage(Update update, FilterType analyzer) {
        DeleteMessage deleteMessage = new DeleteMessage();

        if (analyzer != FilterType.GOOD) {
            int messageId = update.getMessage().getMessageId();

            deleteMessage.setChatId(update.getMessage().getChatId());
            deleteMessage.setMessageId(messageId);
            silentSender.execute(deleteMessage);
            silentSender.send("Message was deleted due to non-compliance. Cause: " + analyzer,
                    update.getMessage().getChatId());
        }
    }

    private FilterType analyzerMassage(String messageText, long chatId) {
        return Analyzer.analyze(messageText, dataBaseManager.getRules(chatId));
    }

    private FilterType analyzerPhoto(List<PhotoSize> photo, long chatId) {
        return Analyzer.analyze(photo, dataBaseManager.getPhotoRules(chatId));
    }
}

