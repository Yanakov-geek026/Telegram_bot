package ru.gasu.yanakov.bot.analyzer.views;

import ru.gasu.yanakov.bot.analyzer.controllers.interfaces.Analyzer;
import ru.gasu.yanakov.bot.analyzer.models.DBRulePhoto;
import ru.gasu.yanakov.bot.analyzer.models.DBRuleText;
import ru.gasu.yanakov.bot.analyzer.publices.types.FilterType;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class MessageAbility implements AbilityExtension {

    private SilentSender silentSender;
    private DBRuleText dbRuleText;
    private DBRulePhoto dbRulePhoto;
    

    public MessageAbility(SilentSender silentSender, DBContext db) {
        this.silentSender = silentSender;
        dbRuleText = new DBRuleText(db);
        dbRulePhoto = new DBRulePhoto(db);
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
            silentSender.send("The message sent by was deleted. Cause: " + analyzer,
                    update.getMessage().getChatId());
        }
    }

    private FilterType analyzerMassage(String messageText, long chatId) {
        return Analyzer.analyze(messageText, dbRuleText.getRules(chatId));
    }

    private FilterType analyzerPhoto(List<PhotoSize> photo, long chatId) {
        return Analyzer.analyze(photo, dbRulePhoto.getPhotoRules(chatId));
    }
}

