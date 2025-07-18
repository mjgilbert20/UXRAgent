import java.util.List;

public class MatchResult {
    private boolean isMatch;
    private double confidence;
    private List<String> matchedStrings;
    private SourceType sourceType;
    private String sourceId;
    private String sourceName;

    public MatchResult(boolean isMatch, double confidence, List<String> matchedStrings) {
        this.isMatch = isMatch;
        this.confidence = confidence;
        this.matchedStrings = matchedStrings;
        this.sourceType = null;
        this.sourceId = null;
        this.sourceName = null;
    }

    public MatchResult(boolean isMatch, double confidence, List<String> matchedStrings, 
                      SourceType sourceType, String sourceId, String sourceName) {
        this.isMatch = isMatch;
        this.confidence = confidence;
        this.matchedStrings = matchedStrings;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
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

    public SourceType getSourceType() {
        return sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceMetadata(SourceType sourceType, String sourceId, String sourceName) {
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
    }

    @Override
    public String toString() {
        return String.format("MatchResult{match=%s, confidence=%.2f, sourceType=%s, sourceName='%s', matches=%d}", 
                           isMatch, confidence, sourceType, sourceName, 
                           matchedStrings != null ? matchedStrings.size() : 0);
    }
}
