import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Utility class for loading different source types from the file system
 * Supports automatic source type detection and batch loading
 */
public class SourceLoader {
    
    /**
     * Load all sources from a directory structure
     */
    public static SourceRepository loadAllSources(String baseDirectory) throws IOException {
        SourceRepository repository = new SourceRepository();
        
        // Load interviews
        loadInterviewsFromDirectory(repository, baseDirectory + "\\interviews");
        
        // Load research reports
        loadReportsFromDirectory(repository, baseDirectory + "\\reports");
        
        // Load documents
        loadDocumentsFromDirectory(repository, baseDirectory + "\\documents");
        
        return repository;
    }
    
    /**
     * Load interview transcripts from directory
     */
    public static void loadInterviewsFromDirectory(SourceRepository repository, String directory) throws IOException {
        Path dirPath = Paths.get(directory);
        if (!Files.exists(dirPath)) {
            System.out.println("Interview directory not found: " + directory);
            return;
        }
        
        try (Stream<Path> files = Files.list(dirPath)) {
            files.filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".txt"))
                 .forEach(path -> {
                     try {
                         String content = Files.readString(path);
                         String filename = path.getFileName().toString();
                         
                         TextTranscript transcript = new TextTranscript(
                             UUID.randomUUID(), 
                             filename, 
                             content, 
                             createMetadata("interview", filename, path.toString())
                         );
                         
                         repository.addSource(transcript);
                         System.out.println("Loaded interview: " + filename);
                         
                     } catch (IOException e) {
                         System.err.println("Failed to load interview: " + path + " - " + e.getMessage());
                     }
                 });
        }
    }
    
    /**
     * Load research reports from directory
     */
    public static void loadReportsFromDirectory(SourceRepository repository, String directory) throws IOException {
        Path dirPath = Paths.get(directory);
        if (!Files.exists(dirPath)) {
            System.out.println("Reports directory not found: " + directory);
            return;
        }
        
        try (Stream<Path> files = Files.list(dirPath)) {
            files.filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".txt"))
                 .forEach(path -> {
                     try {
                         String content = Files.readString(path);
                         String filename = path.getFileName().toString();
                         
                         // Parse basic metadata from content (simplified approach)
                         String title = extractTitle(content, filename);
                         String author = extractAuthor(content);
                         String date = extractDate(content);
                         String methodology = extractMethodology(content);
                         
                         ResearchReport report = new ResearchReport(
                             UUID.randomUUID(),
                             title,
                             content,
                             author,
                             date,
                             methodology,
                             createMetadata("research_report", filename, path.toString())
                         );
                         
                         repository.addSource(report);
                         System.out.println("Loaded research report: " + title);
                         
                     } catch (IOException e) {
                         System.err.println("Failed to load report: " + path + " - " + e.getMessage());
                     }
                 });
        }
    }
    
    /**
     * Load general documents from directory
     */
    public static void loadDocumentsFromDirectory(SourceRepository repository, String directory) throws IOException {
        Path dirPath = Paths.get(directory);
        if (!Files.exists(dirPath)) {
            System.out.println("Documents directory not found: " + directory);
            return;
        }
        
        try (Stream<Path> files = Files.list(dirPath)) {
            files.filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".txt"))
                 .forEach(path -> {
                     try {
                         String content = Files.readString(path);
                         String filename = path.getFileName().toString();
                         
                         // Create a generic document source
                         GenericDocument document = new GenericDocument(
                             UUID.randomUUID(),
                             filename,
                             content,
                             createMetadata("document", filename, path.toString())
                         );
                         
                         repository.addSource(document);
                         System.out.println("Loaded document: " + filename);
                         
                     } catch (IOException e) {
                         System.err.println("Failed to load document: " + path + " - " + e.getMessage());
                     }
                 });
        }
    }
    
    // Helper methods for metadata extraction
    private static String extractTitle(String content, String fallback) {
        // Look for title patterns like "# Title" or "**Title**"
        String[] lines = content.split("\\n");
        for (String line : lines) {
            if (line.startsWith("# ") && !line.startsWith("## ")) {
                return line.substring(2).trim();
            }
        }
        return fallback.replace(".txt", "").replace("_", " ");
    }
    
    private static String extractAuthor(String content) {
        // Look for author patterns
        if (content.contains("**Author:**")) {
            int start = content.indexOf("**Author:**") + 11;
            int end = content.indexOf("\\n", start);
            if (end == -1) end = start + 50; // Fallback
            return content.substring(start, Math.min(end, content.length())).trim();
        }
        if (content.contains("**Authors:**")) {
            int start = content.indexOf("**Authors:**") + 12;
            int end = content.indexOf("\\n", start);
            if (end == -1) end = start + 50;
            return content.substring(start, Math.min(end, content.length())).trim();
        }
        return "Unknown";
    }
    
    private static String extractDate(String content) {
        // Look for date patterns
        if (content.contains("**Date:**")) {
            int start = content.indexOf("**Date:**") + 9;
            int end = content.indexOf("\\n", start);
            if (end == -1) end = start + 20;
            return content.substring(start, Math.min(end, content.length())).trim();
        }
        return "Unknown";
    }
    
    private static String extractMethodology(String content) {
        // Look for methodology patterns
        if (content.contains("**Methodology:**")) {
            int start = content.indexOf("**Methodology:**") + 16;
            int end = content.indexOf("\\n", start);
            if (end == -1) end = start + 100;
            return content.substring(start, Math.min(end, content.length())).trim();
        }
        return "Not specified";
    }
    
    private static Map<String, String> createMetadata(String type, String filename, String fullPath) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("type", type);
        metadata.put("filename", filename);
        metadata.put("fullPath", fullPath);
        metadata.put("loadedAt", new Date().toString());
        return metadata;
    }
}
