import java.util.Map;
import java.util.UUID;

public class TextTranscript extends InterviewSource {
    private String filePath;
    private String content;

    public TextTranscript(UUID id, String filePath, String content, Map<String, String> metadata) {
        this.id = id;
        this.filePath = filePath;
        this.content = content;
        this.metadata = metadata;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public SourceType getType() {
        return SourceType.TEXT;
    }

    public String getFilePath() {
        return filePath;
    }
}
