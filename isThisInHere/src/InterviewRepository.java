import java.util.List;

public class InterviewRepository {
    private List<InterviewSource> sources;

    public InterviewRepository(List<InterviewSource> sources) {
        this.sources = sources;
    }

    public void addSource(InterviewSource src) {
        sources.add(src);
    }

    public List<InterviewSource> getAllSources() {
        return sources;
    }
}
