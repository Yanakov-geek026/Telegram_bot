import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private final String BOT_USERNAME = "Chat_Analyzer_bot";
    private final String BOT_TOKEN = System.getenv("TOKEN");
    private static long chat_id;

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    public Bot(DefaultBotOptions botOptions) {
        super(botOptions);
    }

    @Override
    public void onUpdateReceived(Update update) {
        update.getUpdateId();
        chat_id = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage().setChatId(chat_id);

        String text = update.getMessage().getText();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            if (update.getMessage().getText().equals("Inline")) {
                execute(sendInlineKeyBoardMessage(update.getMessage().getChatId()));
            } else if (text.contains("+") || text.contains("-") || text.contains("/") || text.contains("*")) {
                sendMessage.setText("Result: " + String.valueOf(getMsg(text)));
                execute(sendMessage);
            } else {
                sendMessage.setText(getMessage(text));
                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private float getMsg(String text) {
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
            return Float.parseFloat(engine.eval(text).toString());
        } catch (ScriptException e) {
            return 0;
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    public String getMessage(String msg) {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        if (msg.equals("Hello") || msg.equals("Menu")) {
            keyboardFirstRow.add("Popular");
            keyboardFirstRow.add("News");
            keyboardFirstRow.add("Helpful information");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Select...";
        } else if (msg.equals("Popular")) {
            keyboardFirstRow.add("Music");
            keyboardFirstRow.add("Films");
            keyboardFirstRow.add("Food");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Popular";
        } else if (msg.equals("News")) {
            keyboardFirstRow.add("Sport");
            keyboardFirstRow.add("Politic");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Popular";
        } else if (msg.equals("Helpful information")) {
            keyboardFirstRow.add("Help");
            keyboardFirstRow.add("Commands");
            keyboardFirstRow.add("Information about Bot");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Popular";
        } else {
            return null;
        }
    }


    public static SendMessage sendInlineKeyBoardMessage(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();

        inlineKeyboardButton1.setText("Button1");
        inlineKeyboardButton1.setCallbackData("Button \"Button1\" has been pressed");
        inlineKeyboardButton2.setText("Button2");
        inlineKeyboardButton2.setCallbackData("Button \"Button2\" has been pressed");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();

        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Button3").setCallbackData("CallFi4a"));
        keyboardButtonsRow2.add(inlineKeyboardButton2);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return new SendMessage().setChatId(chatId).setText("Example").setReplyMarkup(inlineKeyboardMarkup);
    }
}
