import java.net.URI; // Import for building the API URI
import java.net.http.HttpClient; // Import for HTTP client
import java.net.http.HttpRequest; // Import for HTTP requests
import java.net.http.HttpResponse; // Import for HTTP responses
import java.nio.file.Files; // Import for reading files from disk
import java.nio.file.Paths; // Import for handling file paths
import java.nio.charset.StandardCharsets; // Import for specifying UTF-8 encoding
import java.io.IOException; // Import for handling file input/output exceptions
import java.util.*; // Import for utility classes like List, ArrayList, HashMap, UUID
import java.nio.file.StandardOpenOption; // Import for file writing options
import org.json.JSONObject;
import org.json.JSONArray;
//Second test text to see if I can get it into the main branch

public class Main {
    public static void main(String[] args) {
        // Parse command line arguments
        if (args.length > 0 && args[0].equals("multi-source")) {
            String projectPath = null;
            
            // Check for project parameter
            for (int i = 1; i < args.length; i++) {
                if (args[i].equals("--project") && i + 1 < args.length) {
                    projectPath = args[i + 1];
                    break;
                }
            }
            
            runMultiSourceDemo(projectPath);
        } else {
            runOriginalDemo();
        }
    }
    
    /**
     * New multi-source demonstration with project management
     */
    public static void runMultiSourceDemo(String projectPath) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    UXR AGENT DEMO                            ║");
        System.out.println("║           Cross-Source Insight Validation                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        
        // Get the API key from environment variables
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("❌ Error: OPENAI_API_KEY environment variable is not set.");
            return;
        }
        
        try {
            ProjectConfig project = null;
            
            if (projectPath != null) {
                // Load specific project
                System.out.println("🎯 Loading project: " + projectPath);
                project = ProjectConfig.load(projectPath);
            } else {
                // Project discovery and selection
                System.out.println("🔍 DISCOVERING PROJECTS");
                System.out.println("══════════════════════");
                
                String defaultProjectsDir = "C:\\Users\\usabi\\IdeaProjects\\uxrAgent\\projects";
                ProjectManager projectManager = new ProjectManager(defaultProjectsDir);
                
                List<ProjectConfig> projects = projectManager.discoverProjects();
                if (projects.isEmpty()) {
                    // Fallback to legacy testData directory
                    System.out.println("No projects found. Using legacy testData directory...");
                    runLegacyMultiSourceDemo();
                    return;
                } else {
                    project = projectManager.selectProject();
                    if (project == null) {
                        System.err.println("❌ No project selected.");
                        return;
                    }
                }
            }
            
            // Display project information
            ProjectManager manager = new ProjectManager(project.getBaseDirectory());
            manager.displayProjectStats(project);
            
            System.out.println("� PHASE 1: LOADING RESEARCH SOURCES");
            System.out.println("════════════════════════════════════");
            
            // Load all sources using the project's data directory
            SourceRepository repository = SourceLoader.loadAllSources(project.getDataDirectory());
            
            // Display repository statistics
            System.out.println("\n📊 REPOSITORY OVERVIEW");
            System.out.println("─────────────────────");
            
            Map<SourceType, Integer> typeCounts = new HashMap<>();
            for (SourceType type : SourceType.values()) {
                int count = repository.getSourcesByType(type).size();
                typeCounts.put(type, count);
                if (count > 0) {
                    System.out.println(getSourceIcon(type) + " " + type + ": " + count + " sources");
                }
            }
            System.out.println("📚 Total sources: " + repository.getAllSources().size());
            
            // Continue with semantic search and validation
            runSemanticValidation(repository, project);
            
        } catch (IOException e) {
            System.err.println("❌ Error loading project: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Legacy demo for backward compatibility
     */
    public static void runLegacyMultiSourceDemo() {
        try {
            System.out.println("\n📂 PHASE 1: LOADING RESEARCH SOURCES (Legacy Mode)");
            System.out.println("═══════════════════════════════════════════════════");
            
            // Load all sources using the legacy SourceLoader
            String testDataDir = "C:\\Users\\usabi\\IdeaProjects\\uxrAgent\\testData";
            SourceRepository repository = SourceLoader.loadAllSources(testDataDir);
            
            runSemanticValidation(repository, null);
            
        } catch (IOException e) {
            System.err.println("❌ Error in legacy demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Run semantic validation process
     */
    public static void runSemanticValidation(SourceRepository repository, ProjectConfig project) {
        try {
            // Get API key
            String apiKey = System.getenv("OPENAI_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                System.err.println("❌ Error: OPENAI_API_KEY environment variable is not set.");
                return;
            }
            
            System.out.println("📊 REPOSITORY OVERVIEW");
            System.out.println("─────────────────────");
            Map<SourceType, Integer> counts = repository.getSourceCounts();
            for (Map.Entry<SourceType, Integer> entry : counts.entrySet()) {
                if (entry.getValue() > 0) {
                    String icon = getSourceIcon(entry.getKey());
                    System.out.println(icon + " " + entry.getKey() + ": " + entry.getValue() + " sources");
                }
            }
            System.out.println("� Total sources: " + repository.getTotalSourceCount());
            
            // Define the insight to search for
            String insight = "users maintain multiple email accounts for different purposes";
            
            System.out.println("\n� PHASE 2: SEMANTIC SEARCH & VALIDATION");
            System.out.println("═══════════════════════════════════════");
            System.out.println("❓ Research Question:");
            System.out.println("   \"" + insight + "\"");
            System.out.println("\n🤖 Running AI-powered semantic analysis...");
            
            // Use the enhanced SemanticMatcher with LLM
            SemanticMatcher matcher = new SemanticMatcher();
            List<MatchResult> results = matcher.matchAcrossSourcesWithLLM(apiKey, insight, repository.getAllSources());
            
            // Display results by source type
            System.out.println("\n📋 VALIDATION RESULTS");
            System.out.println("═══════════════════");
            int matchCount = 0;
            for (MatchResult result : results) {
                if (result.isMatch()) {
                    matchCount++;
                    String icon = getSourceIcon(result.getSourceType());
                    System.out.println("\n🔍 EVIDENCE FOUND #" + matchCount);
                    System.out.println("   Source Type: " + result.getSourceType());
                    System.out.println("   Source: " + result.getSourceName());
                    System.out.println("   Confidence: " + String.format("%.0f%%", result.getConfidence() * 100));
                    System.out.println("   Supporting Text:");
                    System.out.println("   ┌─────────────────────────────────────────────────────────────┐");
                    String wrappedText = wrapText(result.getMatchedStrings().get(0), 60);
                    for (String line : wrappedText.split("\n")) {
                        System.out.println("   │ " + String.format("%-58s", line) + " │");
                    }
                    System.out.println("   └─────────────────────────────────────────────────────────────┘");
                }
            }
            
            if (matchCount == 0) {
                System.out.println("❌ No semantic matches found across sources.");
            } else {
                System.out.println("\n📊 SUMMARY");
                System.out.println("═════════");
                System.out.println("✅ Insight VALIDATED across " + matchCount + " different source types");
                System.out.println("🎯 Confidence: HIGH (AI + Multiple Sources)");
                System.out.println("🔗 Full traceability maintained");
            }
            
            // Demonstrate type-specific filtering
            System.out.println("=== Research Reports Only ===");
            List<InterviewSource> reportSources = repository.getSourcesByType(SourceType.RESEARCH_REPORT);
            for (InterviewSource source : reportSources) {
                if (source instanceof ResearchReport) {
                    ResearchReport report = (ResearchReport) source;
                    System.out.println("Report: " + report.getTitle());
                    System.out.println("Author: " + report.getAuthor());
                    System.out.println("Date: " + report.getPublicationDate());
                    System.out.println();
                }
            }
            
            // Save results to project directory if available, otherwise to testData
            String outputPath;
            if (project != null) {
                outputPath = project.getResultsDirectory() + "\\analysis_results.txt";
            } else {
                outputPath = "testData\\multi_source_analysis.txt";
            }
            
            StringBuilder output = new StringBuilder();
            output.append("=== UXR Agent Multi-Source Analysis ===\n");
            if (project != null) {
                output.append("Project: ").append(project.getName()).append("\n");
                output.append("Project ID: ").append(project.getProjectId()).append("\n");
            }
            output.append("Insight: ").append(insight).append("\n");
            output.append("Total Sources: ").append(repository.getTotalSourceCount()).append("\n");
            output.append("Source Distribution:\n");
            for (Map.Entry<SourceType, Integer> entry : counts.entrySet()) {
                if (entry.getValue() > 0) {
                    output.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
            }
            output.append("\nMatches Found:\n");
            for (MatchResult result : results) {
                if (result.isMatch()) {
                    output.append("  ").append(result.toString()).append("\n");
                }
            }
            
            Files.write(
                Paths.get(outputPath),
                output.toString().getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
            );
            
            System.out.println("Analysis results saved to " + outputPath);
            
        } catch (IOException e) {
            System.err.println("❌ Error in semantic validation: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Helper methods for enhanced console output
    private static String getSourceIcon(SourceType sourceType) {
        switch (sourceType) {
            case TEXT_TRANSCRIPT: return "💬";
            case RESEARCH_REPORT: return "📊";
            case DOCUMENT: return "📄";
            case VIDEO_INTERVIEW: return "🎥";
            case SURVEY_RESPONSE: return "📝";
            case USER_FEEDBACK: return "💭";
            case ACADEMIC_PAPER: return "🎓";
            case FIELD_NOTES: return "📋";
            default: return "📁";
        }
    }
    
    private static String wrapText(String text, int width) {
        if (text.length() <= width) return text;
        
        StringBuilder wrapped = new StringBuilder();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        
        for (String word : words) {
            if (line.length() + word.length() + 1 > width) {
                wrapped.append(line.toString().trim()).append("\n");
                line = new StringBuilder();
            }
            line.append(word).append(" ");
        }
        wrapped.append(line.toString().trim());
        
        return wrapped.toString();
    }
    
    /**
     * Original single-source demonstration (preserved for backward compatibility)
     */
    public static void runOriginalDemo() {
        // Get the API key from environment variables
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Error: OPENAI_API_KEY environment variable is not set.");
            return;
        }
        // Ingest transcript files into a map for batch and single-file support
        String baseDir = "C:\\Users\\usabi\\IdeaProjects\\uxrAgent\\isThisInHereTestFiles\\";
        String[] filenames = {"interview1.txt", "interview2.txt", "interview3.txt", "interview4.txt", "interview5.txt"};
        Map<String, String> transcripts = new HashMap<>();
        for (String filename : filenames) {
            String path = baseDir + filename;
            try {
                String content = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
                transcripts.put(filename, content);
            } catch (IOException e) {
                System.err.println("Failed to read " + filename + ": " + e.getMessage());
            }
        }

        // Create a list of InterviewSource objects from the loaded transcripts
        List<InterviewSource> sources = new ArrayList<>();
        for (Map.Entry<String, String> entry : transcripts.entrySet()) {
            sources.add(new TextTranscript(UUID.randomUUID(), entry.getKey(), entry.getValue(), new HashMap<>()));
        }

        // Stub for future extensibility: sources can be extended with other types
        // Example: sources.add(new VideoInterview(...));

        // Define the insight (the string you want to search for in the transcripts)
        String insight = "R: Uh, yeah, quite a few actually. Two emails — one for work and one personal. Online banking, obviously. Social media — Instagram, Facebook, Twitter… well, I guess it’s X now. I also have a few shopping accounts like Amazon and eBay. Oh, and I use MyChart for health stuff.";

        // Prepare output as a StringBuilder
        StringBuilder output = new StringBuilder();
        // Call the LLM to rephrase the insight and write to output file
        String rephrasedInsight = rephraseWithLLM(apiKey, insight);
        output.append("Rephrased Insight: ").append(rephrasedInsight).append(System.lineSeparator());
        output.append("====").append(System.lineSeparator());
        // Also write the rephrased insight to outputtest.txt for verification
        try {
            Files.write(
                Paths.get("C:\\Users\\usabi\\IdeaProjects\\uxrAgent\\isThisInHereTestFiles\\outputtest.txt"),
                rephrasedInsight.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            System.err.println("Failed to write outputtest.txt: " + e.getMessage());
        }

        // Semantic matching: check each transcript for the insight using the LLM
        for (InterviewSource source : sources) {
            boolean found = isInsightInTranscriptWithLLM(apiKey, insight, source.getContent());
            String sourceName = (source instanceof TextTranscript) ? ((TextTranscript)source).getFilePath() : source.getClass().getSimpleName();
            output.append("Source: ").append(sourceName).append(System.lineSeparator());
            output.append("Insight found (LLM): ").append(found).append(System.lineSeparator());
            output.append("---").append(System.lineSeparator());
        }

        // Write the output to Interview1output.txt in the same directory as interview1.txt
        try {
            Files.write(
                Paths.get("C:\\Users\\usabi\\IdeaProjects\\uxrAgent\\isThisInHereTestFiles\\Interview1output.txt"),
                output.toString().getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            System.err.println("Failed to write output file: " + e.getMessage());
        }
    }

    // Use the LLM to check for semantic similarity between an insight and a transcript
    public static boolean isInsightInTranscriptWithLLM(String apiKey, String insight, String transcript) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String resourceName = "OpenAIresourceForPOC";
            String deploymentId = "gpt-35-turbo";
            String apiVersion = "2024-02-15-preview";
            String endpoint = "https://" + resourceName +
                ".openai.azure.com/openai/deployments/" + deploymentId +
                "/chat/completions?api-version=" + apiVersion;

            String prompt = "Does the following transcript contain the same idea as the insight? " +
                    "Reply only with 'yes' or 'no'.\n\nInsight:\n" + insight + "\n\nTranscript:\n" + transcript;

            String systemPrompt = "You are a helpful assistant that checks for semantic similarity between an insight and a transcript.";
            String userPrompt = prompt;
            String requestBody = "{" +
                "\"messages\": [" +
                "{\"role\": \"system\", \"content\": " + JSONObject.quote(systemPrompt) + "}," +
                "{\"role\": \"user\", \"content\": " + JSONObject.quote(userPrompt) + "}" +
                "]," +
                "\"max_tokens\": 5" +
            "}";

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .header("api-key", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            JSONObject obj = new JSONObject(responseBody);
            if (obj.has("error")) {
                System.err.println("[Azure OpenAI API error: " + obj.getJSONObject("error").getString("message") + "]");
                return false;
            }
            if (!obj.has("choices")) {
                System.err.println("[Azure OpenAI API: No 'choices' field in response!]");
                return false;
            }
            JSONArray choices = obj.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject message = choices.getJSONObject(0).getJSONObject("message");
                String content = message.getString("content").trim().toLowerCase();
                return content.startsWith("yes");
            }
        } catch (Exception e) {
            System.err.println("[Error calling Azure LLM for semantic match: " + e.getMessage() + "]");
        }
        return false;
    }
    // Utility method to load a single transcript file
    public static InterviewSource loadTranscript(String filePath) throws IOException {
        String content = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
        return new TextTranscript(UUID.randomUUID(), Paths.get(filePath).getFileName().toString(), content, new HashMap<>());
    }

    // Sample method to call OpenAI API and rephrase the insight string
    public static String rephraseWithLLM(String apiKey, String insight) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            // Azure OpenAI endpoint details - update these with your actual values
            String resourceName = "OpenAIresourceForPOC"; // e.g., my-azure-openai
            String deploymentId = "gpt-35-turbo"; // e.g., gpt-35-turbo-deployment
            String apiVersion = "2024-02-15-preview";
            String endpoint = "https://" + resourceName +
                ".openai.azure.com/openai/deployments/" + deploymentId +
                "/chat/completions?api-version=" + apiVersion;

            String systemPrompt = "You are a helpful assistant that rephrases text.";
            String userPrompt = "Rephrase the following: " + insight;
            String requestBody = "{" +
                "\"messages\": [" +
                "{\"role\": \"system\", \"content\": " + JSONObject.quote(systemPrompt) + "}," +
                "{\"role\": \"user\", \"content\": " + JSONObject.quote(userPrompt) + "}" +
                "]," +
                "\"max_tokens\": 200" +
            "}";
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .header("api-key", apiKey) // Azure uses "api-key" header
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            // Print the full response for debugging
            System.out.println("Azure OpenAI API response: " + responseBody);
            JSONObject obj = new JSONObject(responseBody);
            if (obj.has("error")) {
                return "[Azure OpenAI API error: " + obj.getJSONObject("error").getString("message") + "]";
            }
            JSONArray choices = obj.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject message = choices.getJSONObject(0).getJSONObject("message");
                return message.getString("content").trim();
            }
            return "[No rephrased text found]";
        } catch (Exception e) {
            return "[Error calling Azure LLM: " + e.getMessage() + "]";
        }
    }
}
