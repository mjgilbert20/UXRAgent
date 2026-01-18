import java.util.List;
import java.util.ArrayList;

public class SemanticMatcher {
    
    /**
     * Basic semantic matching with source metadata
     */
    public MatchResult match(String insight, String content, InterviewSource source) {
        // Extract source metadata
        SourceType sourceType = determineSourceType(source);
        String sourceId = source.getId().toString();
        String sourceName = getSourceName(source);
        
        // Use simple string matching as fallback
        boolean isMatch = content.toLowerCase().contains(insight.toLowerCase());
        double confidence = isMatch ? 0.8 : 0.2;
        
        List<String> matchedStrings = new ArrayList<>();
        if (isMatch) {
            matchedStrings.add(insight);
        }
        
        return new MatchResult(isMatch, confidence, matchedStrings, sourceType, sourceId, sourceName);
    }
    
    /**
     * LLM-powered semantic matching across sources with API key
     */
    public MatchResult matchWithLLM(String apiKey, String insight, InterviewSource source) {
        // Extract source metadata
        SourceType sourceType = determineSourceType(source);
        String sourceId = source.getId().toString();
        String sourceName = getSourceName(source);
        
        // Use the existing LLM logic from Main.java
        boolean isMatch = callLLMForSemanticMatch(apiKey, insight, source.getContent());
        double confidence = isMatch ? 0.9 : 0.1; // Higher confidence for LLM matches
        
        List<String> matchedStrings = new ArrayList<>();
        if (isMatch) {
            // Extract the relevant text segment that matched
            String extractedText = extractMatchingSegment(source.getContent(), insight);
            matchedStrings.add(extractedText);
        }
        
        return new MatchResult(isMatch, confidence, matchedStrings, sourceType, sourceId, sourceName);
    }
    
    /**
     * Batch LLM matching across multiple sources
     */
    public List<MatchResult> matchAcrossSourcesWithLLM(String apiKey, String insight, List<InterviewSource> sources) {
        List<MatchResult> results = new ArrayList<>();
        
        System.out.println("Running LLM-powered semantic matching across " + sources.size() + " sources...");
        
        for (InterviewSource source : sources) {
            MatchResult result = matchWithLLM(apiKey, insight, source);
            results.add(result);
            
            // Progress indicator
            if (result.isMatch()) {
                System.out.println("✓ Match found in " + result.getSourceType() + ": " + result.getSourceName());
            }
        }
        
        return results;
    }
    
    private boolean callLLMForSemanticMatch(String apiKey, String insight, String content) {
        try {
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            String resourceName = "OpenAIresourceForPOC";
            String deploymentId = "gpt-35-turbo";
            String apiVersion = "2024-02-15-preview";
            String endpoint = "https://" + resourceName +
                ".openai.azure.com/openai/deployments/" + deploymentId +
                "/chat/completions?api-version=" + apiVersion;

            String prompt = "Does the following content contain the same idea or support this insight? " +
                    "Reply only with 'yes' or 'no'.\n\nInsight:\n" + insight + "\n\nContent:\n" + content;

            String systemPrompt = "You are a UX research assistant that identifies semantic similarity between insights and source materials.";
            String requestBody = "{" +
                "\"messages\": [" +
                "{\"role\": \"system\", \"content\": " + org.json.JSONObject.quote(systemPrompt) + "}," +
                "{\"role\": \"user\", \"content\": " + org.json.JSONObject.quote(prompt) + "}" +
                "]," +
                "\"max_tokens\": 5" +
            "}";

            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(endpoint))
                .header("Content-Type", "application/json")
                .header("api-key", apiKey)
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            org.json.JSONObject obj = new org.json.JSONObject(responseBody);
            
            if (obj.has("error")) {
                System.err.println("Azure OpenAI API error: " + obj.getJSONObject("error").getString("message"));
                return false;
            }
            if (!obj.has("choices")) {
                return false;
            }
            org.json.JSONArray choices = obj.getJSONArray("choices");
            if (choices.length() > 0) {
                org.json.JSONObject message = choices.getJSONObject(0).getJSONObject("message");
                String responseContent = message.getString("content").trim().toLowerCase();
                return responseContent.startsWith("yes");
            }
        } catch (Exception e) {
            System.err.println("Error in LLM semantic matching: " + e.getMessage());
        }
        return false;
    }
    
    private String extractMatchingSegment(String content, String insight) {
        // Simple extraction - find the sentence containing key terms from insight
        String[] sentences = content.split("\\. ");
        String[] insightWords = insight.toLowerCase().split("\\s+");
        
        for (String sentence : sentences) {
            String lowerSentence = sentence.toLowerCase();
            int matches = 0;
            for (String word : insightWords) {
                if (lowerSentence.contains(word) && word.length() > 3) { // Skip short words
                    matches++;
                }
            }
            if (matches >= 2) { // If sentence contains multiple insight terms
                return sentence.trim() + ".";
            }
        }
        
        // Fallback: return first 200 characters
        return content.length() > 200 ? content.substring(0, 200) + "..." : content;
    }
    
    /**
     * Legacy method for backward compatibility
     */
    public MatchResult match(String thisStr, String hereStr) {
        return new MatchResult(false, 0.0, null);
    }
    
    /**
     * Batch matching across multiple sources with type filtering
     */
    public List<MatchResult> matchAcrossSources(String insight, List<InterviewSource> sources) {
        List<MatchResult> results = new ArrayList<>();
        
        for (InterviewSource source : sources) {
            MatchResult result = match(insight, source.getContent(), source);
            results.add(result);
        }
        
        return results;
    }
    
    /**
     * Source type-aware matching with different strategies per type
     */
    public MatchResult matchWithTypeStrategy(String insight, InterviewSource source) {
        SourceType type = determineSourceType(source);
        
        switch (type) {
            case TEXT_TRANSCRIPT:
                return matchTranscript(insight, source);
            case RESEARCH_REPORT:
                return matchReport(insight, source);
            case DOCUMENT:
                return matchDocument(insight, source);
            default:
                return match(insight, source.getContent(), source);
        }
    }
    
    private MatchResult matchTranscript(String insight, InterviewSource source) {
        // Specialized matching for interview transcripts
        // Could consider speaker patterns, dialogue structure, etc.
        return match(insight, source.getContent(), source);
    }
    
    private MatchResult matchReport(String insight, InterviewSource source) {
        // Specialized matching for research reports
        // Could consider sections, citations, methodology, etc.
        return match(insight, source.getContent(), source);
    }
    
    private MatchResult matchDocument(String insight, InterviewSource source) {
        // Specialized matching for general documents
        return match(insight, source.getContent(), source);
    }
    
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
        return SourceType.DOCUMENT;
    }
    
    private String getSourceName(InterviewSource source) {
        if (source instanceof TextTranscript) {
            return ((TextTranscript) source).getFilePath();
        }
        return source.getClass().getSimpleName();
    }
}
