import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class HelloAbilityBot extends AbilityBot {

    private static final String BOT_USERNAME = "Chat_Analyzer_bot";
    private static final String BOT_TOKEN = System.getenv("TOKEN");

    HelloAbilityBot(DefaultBotOptions botOptions) {
        super(BOT_TOKEN, BOT_USERNAME, botOptions);
    }

    @Override
    public int creatorId() {
        return 0;
    }

    public Ability sayHelloWorld() {
        return Ability
                .builder()
                .name("num")
                .input(1)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)

                .action(ctx -> {
                    String text = ctx.firstArg();
                    //ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
                    try {
                        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
                        float answer = Float.parseFloat(engine.eval(text).toString());
                        silent.send( String.valueOf(answer), ctx.chatId());
                    } catch (ScriptException e) {
                        e.printStackTrace();
                    }
                })

                .post(ctx -> silent.send("Bay Bay", ctx.chatId()))

                .build();
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

