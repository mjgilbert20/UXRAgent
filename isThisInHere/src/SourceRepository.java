import java.util.*;
import java.util.stream.Collectors;

/**
 * Generalized repository for managing multiple source types
 * Replaces the interview-specific repository with multi-source capability
 */
public class SourceRepository {
    private Map<SourceType, List<InterviewSource>> sourcesByType;
    private List<InterviewSource> allSources;

    public SourceRepository() {
        this.sourcesByType = new HashMap<>();
        this.allSources = new ArrayList<>();
        
        // Initialize empty lists for each source type
        for (SourceType type : SourceType.values()) {
            sourcesByType.put(type, new ArrayList<>());
        }
    }

    /**
     * Add a source to the repository with automatic type classification
     */
    public void addSource(InterviewSource source) {
        allSources.add(source);
        SourceType type = determineSourceType(source);
        sourcesByType.get(type).add(source);
    }

    /**
     * Get all sources regardless of type
     */
    public List<InterviewSource> getAllSources() {
        return new ArrayList<>(allSources);
    }

    /**
     * Get sources filtered by type
     */
    public List<InterviewSource> getSourcesByType(SourceType type) {
        return new ArrayList<>(sourcesByType.get(type));
    }

    /**
     * Get sources filtered by multiple types
     */
    public List<InterviewSource> getSourcesByTypes(Set<SourceType> types) {
        return allSources.stream()
                .filter(source -> types.contains(determineSourceType(source)))
                .collect(Collectors.toList());
    }

    /**
     * Get summary statistics about the repository
     */
    public Map<SourceType, Integer> getSourceCounts() {
        Map<SourceType, Integer> counts = new HashMap<>();
        for (SourceType type : SourceType.values()) {
            counts.put(type, sourcesByType.get(type).size());
        }
        return counts;
    }

    /**
     * Search across all sources with optional type filtering
     */
    public List<InterviewSource> searchSources(String query, Set<SourceType> filterTypes) {
        List<InterviewSource> searchScope = filterTypes.isEmpty() ? 
            allSources : getSourcesByTypes(filterTypes);
        
        return searchScope.stream()
                .filter(source -> source.getContent().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Determine source type based on source characteristics
     * This can be enhanced with more sophisticated classification logic
     */
    private SourceType determineSourceType(InterviewSource source) {
        if (source instanceof TextTranscript) {
            return SourceType.TEXT_TRANSCRIPT;
        } else if (source instanceof VideoInterview) {
            return SourceType.VIDEO_INTERVIEW;
        } else if (source instanceof ResearchReport) {
            return SourceType.RESEARCH_REPORT;
        } else if (source instanceof GenericDocument) {
            return SourceType.DOCUMENT;
        }
        // For future extensibility - could analyze content, file extension, metadata, etc.
        return SourceType.DOCUMENT; // Default fallback
    }

    /**
     * Get total number of sources across all types
     */
    public int getTotalSourceCount() {
        return allSources.size();
    }

    /**
     * Check if repository contains sources of a specific type
     */
    public boolean hasSourcesOfType(SourceType type) {
        return !sourcesByType.get(type).isEmpty();
    }
}
