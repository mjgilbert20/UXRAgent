public class VerificationResult {
    private InterviewSource source;
    private MatchResult matchResult;

    public VerificationResult(InterviewSource source, MatchResult matchResult) {
        this.source = source;
        this.matchResult = matchResult;
    }

    public InterviewSource getSource() {
        return source;
    }

    public MatchResult getMatchResult() {
        return matchResult;
    }
}
