package analyzerBot.AnalyzerInterface;

import java.io.Serializable;

public abstract class ControlRules<T> implements Analyzer<T>, Serializable {

    protected boolean ruleBoolean;

    public abstract void onRule();

    public abstract void offRule();

    public abstract boolean checkActivateRule();

    public abstract String manualRules();
}
