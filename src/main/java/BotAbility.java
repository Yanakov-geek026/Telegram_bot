import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

public class BotAbility implements AbilityExtension {
    private SilentSender silentSender;
    private DBContext db;
    private User user;

    BotAbility(SilentSender silentSender, DBContext db) {
        this.silentSender = silentSender;
        this.db = db;
    }

    public Ability regulations() {
        return Ability
                .builder()
                .name("regulations")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(0)
                .action(ctx -> {
                    String regulations = "You can not use emoticons (Cause USE_SMILE)" +
                            "\n" + "Cannot send phone number (Cause PHONE_NUMBER)" +
                            "\n" + "Cannot send message more than 20 characters (Cause LONG_TEXT)" +
                            "\n" + "Do not send various links(Cause REPOST_LINK)";
                    silentSender.send(regulations, ctx.chatId());
                })
                .build();
    }

    public Reply message() {
        return Reply.of(update -> {
            long chatId = update.getMessage().getChatId();
            //Rules rules = new Rules(db, chatId);
            String massageText = update.getMessage().getText();
            //FilterType analyzerMessage = rules.analyzerMessage(massageText);
            FilterType analyzerMessage = analyzerMassage(massageText);
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

    public Reply photo() {
        return Reply.of(update -> {
            silentSender.send("Photo", update.getMessage().getChatId());

        }, Flag.PHOTO);
    }

    private FilterType analyzerMassage(String messageText) {
        List<Analyzer> manager = new ArrayList<>();
        manager.add()
//        manager.add(Analyzer.linkCheckTextAnalyzer());
//        manager.add(Analyzer.longCheckTextAnalyzer(20));
//        manager.add(Analyzer.phoneNumberCheckTextAnalyzer());
//        manager.add(Analyzer.smileCheckTextAnalyzer());

        return new FilterManager(manager).analyzer(messageText);
    }
}
