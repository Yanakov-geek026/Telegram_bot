import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;


public class Bot extends TelegramLongPollingBot {

    private final String BOT_USERNAME = "Chat_Analyzer_bot";
    private final String BOT_TOKEN = System.getenv("TOKEN");

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    Bot(DefaultBotOptions botOptions) {
        super(botOptions);
    }

    @Override
    public void onUpdateReceived(Update update) {

        long chat_id = update.getMessage().getChatId();
        SendMessage message = new SendMessage().setChatId(chat_id);
        regulations();
        try {
            if (update.getMessage().getText().equals("regulation")) {
               execute(message.setText(showRegulations()));
            } else {
                if (update.hasMessage() && update.getMessage().hasText()) {
                    message.setText("this text");
                    execute(message);

                    String message_text = update.getMessage().getText();
                    FilterType analyzerMessage = analyzerMessage(message_text);
                    if (analyzerMessage != FilterType.GOOD) {
                        int message_id = update.getMessage().getMessageId();

                        DeleteMessage deleteMessage = new DeleteMessage();
                        deleteMessage.setChatId(chat_id);
                        deleteMessage.setMessageId(message_id);

                        message.setText("Message was deleted due to non-compliance. Cause: " + analyzerMessage);
                        execute(deleteMessage);
                        execute(message);
                    }
                } else {
                    message.setText("this Photo");
                    execute(message);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private FilterType analyzerMessage(String message_text) {
        Analyzer[] manager = new Analyzer[]{Analyzer.smileCheckTextAnalyzer(),
                Analyzer.longCheckTextAnalyzer(20), Analyzer.phoneNumberCheckTextAnalyzer(),
                Analyzer.linkCheckTextAnalyzer()};

        for (Analyzer analyzer : manager) {
            FilterType answer = analyzer.TextAnalyzer(message_text);

            if (answer != FilterType.GOOD) {
                return answer;
            }
        }
        return FilterType.GOOD;
    }

    private void regulations() {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        keyboardFirstRow.add("Regulation");
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    private String showRegulations() {
        String regulation = "You can not use emoticons (Cause USE_SMILE)" +
                        "\n" + "Cannot send phone number (Cause PHONE_NUMBER)" +
                        "\n" + "Cannot send message more than 20 characters (Cause LONG_TEXT)" +
                        "\n" + "Do not poison various links(Cause REPOST_LINK)";
        return regulation;
    }


    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
