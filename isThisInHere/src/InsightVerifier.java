import java.util.ArrayList;
import java.util.List;

public class InsightVerifier {
    private SemanticMatcher matcher;

    public InsightVerifier(SemanticMatcher matcher) {
        this.matcher = matcher;
    }

    public List<VerificationResult> verify(String thisStr, InterviewRepository repo) {
        List<VerificationResult> results = new ArrayList<>();
        for (InterviewSource src : repo.getAllSources()) {
            MatchResult matchResult = matcher.match(thisStr, src.getContent());
            results.add(new VerificationResult(src, matchResult));
        }
        return results;
    }
}
