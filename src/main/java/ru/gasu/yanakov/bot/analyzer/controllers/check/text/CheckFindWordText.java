package ru.gasu.yanakov.bot.analyzer.controllers.check.text;

import ru.gasu.yanakov.bot.analyzer.controllers.interfaces.ControlRules;
import ru.gasu.yanakov.bot.analyzer.publices.types.FilterType;

import java.io.Serializable;

public class CheckFindWordText extends ControlRules<String> implements Serializable {

    private String word;

    public CheckFindWordText(String word) {
        this.word = word;
        ruleBoolean = true;
    }

    @Override
    public boolean check(String message) {
        return message.contains(word);
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.FORBIDDEN_WORD;
    }

    @Override
    public void onRule() {
        ruleBoolean = true;
    }

    @Override
    public void offRule() {
        ruleBoolean = false;
    }

    @Override
    public boolean checkActivateRule() {
        return ruleBoolean;
    }

    @Override
    public String manualRules() {
        return (ruleBoolean? "(ON)": "(OFF)") + " You can't use word a (" + word + ") " + FilterType.FORBIDDEN_WORD + "\n";
    }
}
