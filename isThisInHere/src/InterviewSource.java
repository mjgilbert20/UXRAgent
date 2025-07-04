import java.util.Map;
import java.util.UUID;

public abstract class InterviewSource {
    protected UUID id;
    protected Map<String, String> metadata;
    public abstract String getContent();
    public abstract SourceType getType();
    public UUID getId() { return id; }
    public Map<String, String> getMetadata() { return metadata; }
}
