package ru.gasu.yanakov.bot.analyzer.models;

import org.telegram.abilitybots.api.db.DBContext;
import ru.gasu.yanakov.bot.analyzer.publices.types.FilterType;

public interface DBManager {

    void remove(long chatId);

    String switchRule(int position, boolean status, long chatId);

    String getManualAllRules(long chatId);

    void setDBRule(long chatId);
}
