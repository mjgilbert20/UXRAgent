import java.util.List;

public class MatchResult {
    private boolean isMatch;
    private double confidence;
    private List<String> matchedStrings;

    public MatchResult(boolean isMatch, double confidence, List<String> matchedStrings) {
        this.isMatch = isMatch;
        this.confidence = confidence;
        this.matchedStrings = matchedStrings;
    }

    public boolean isMatch() {
        return isMatch;
    }

    public double getConfidence() {
        return confidence;
    }

    public List<String> getMatchedStrings() {
        return matchedStrings;
    }
}
