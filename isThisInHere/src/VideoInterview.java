import java.util.Map;
import java.util.UUID;

public class VideoInterview extends InterviewSource {
    private String videoPath;
    private String transcript;

    public VideoInterview(UUID id, String videoPath, String transcript, Map<String, String> metadata) {
        this.id = id;
        this.videoPath = videoPath;
        this.transcript = transcript;
        this.metadata = metadata;
    }

    @Override
    public String getContent() {
        return transcript;
    }

    @Override
    public SourceType getType() {
        return SourceType.VIDEO_INTERVIEW;
    }

    public String getVideoPath() {
        return videoPath;
    }
}
