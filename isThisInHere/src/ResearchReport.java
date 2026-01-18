import java.util.Map;
import java.util.UUID;

/**
 * Represents a research report source
 * Demonstrates extensibility for non-interview sources
 */
public class ResearchReport extends InterviewSource {
    private String title;
    private String content;
    private String author;
    private String publicationDate;
    private String methodology;

    public ResearchReport(UUID id, String title, String content, String author, 
                         String publicationDate, String methodology, Map<String, String> metadata) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.publicationDate = publicationDate;
        this.methodology = methodology;
        this.metadata = metadata;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public SourceType getType() {
        return SourceType.RESEARCH_REPORT;
    }

    // Research report specific getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getMethodology() {
        return methodology;
    }

    @Override
    public String toString() {
        return String.format("ResearchReport{id=%s, title='%s', author='%s', date='%s'}", 
                           id, title, author, publicationDate);
    }
}
