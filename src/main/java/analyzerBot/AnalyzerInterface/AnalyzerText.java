package analyzerBot.AnalyzerInterface;

import analyzerBot.Types.TypeMessage;

public interface AnalyzerText extends Analyzer<String> {
    default TypeMessage getContentType() {
        return TypeMessage.TEXT_MESSAGE;
    }
}
