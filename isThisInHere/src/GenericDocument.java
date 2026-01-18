import java.util.Map;
import java.util.UUID;

/**
 * Represents a generic document source
 * Used for documents that don't fit specific categories like research reports
 */
public class GenericDocument extends InterviewSource {
    private String title;
    private String content;

    public GenericDocument(UUID id, String title, String content, Map<String, String> metadata) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.metadata = metadata;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public SourceType getType() {
        return SourceType.DOCUMENT;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return String.format("GenericDocument{id=%s, title='%s'}", id, title);
    }
}
