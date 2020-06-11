package ru.gasu.yanakov.bot.analyzer.models;

import org.telegram.abilitybots.api.db.DBContext;

public interface DBManager {

    void remove(long chatId);

    String switchRule(int position, boolean status, long chatId);

    String getManualAllRules(long chatId);

    void setDBRule(long chatId);
}
