import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import org.json.JSONObject;

/**
 * Configuration class for managing research projects
 * Handles project metadata, directory structure, and settings
 */
public class ProjectConfig {
    private String projectId;
    private String name;
    private String description;
    private String baseDirectory;
    private String dataDirectory;
    private String resultsDirectory;
    private Map<String, String> metadata;
    private Date createdDate;
    private Date lastModified;

    public ProjectConfig(String projectId, String name, String description, String baseDirectory) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.baseDirectory = baseDirectory;
        this.dataDirectory = baseDirectory + "\\sources";
        this.resultsDirectory = baseDirectory + "\\results";
        this.metadata = new HashMap<>();
        this.createdDate = new Date();
        this.lastModified = new Date();
    }

    /**
     * Load project configuration from a project.json file
     */
    public static ProjectConfig load(String projectPath) throws IOException {
        Path configPath = Paths.get(projectPath, "project.json");
        
        if (!Files.exists(configPath)) {
            throw new IOException("Project configuration not found: " + configPath);
        }
        
        String jsonContent = Files.readString(configPath);
        JSONObject json = new JSONObject(jsonContent);
        
        ProjectConfig config = new ProjectConfig(
            json.getString("projectId"),
            json.getString("name"),
            json.optString("description", ""),
            projectPath
        );
        
        // Load metadata if present
        if (json.has("metadata")) {
            JSONObject metadataJson = json.getJSONObject("metadata");
            for (String key : metadataJson.keySet()) {
                config.metadata.put(key, metadataJson.getString(key));
            }
        }
        
        return config;
    }

    /**
     * Save project configuration to project.json file
     */
    public void save() throws IOException {
        Path projectDir = Paths.get(baseDirectory);
        Files.createDirectories(projectDir);
        
        // Create directory structure
        Files.createDirectories(Paths.get(dataDirectory));
        Files.createDirectories(Paths.get(dataDirectory, "interviews"));
        Files.createDirectories(Paths.get(dataDirectory, "reports"));
        Files.createDirectories(Paths.get(dataDirectory, "documents"));
        Files.createDirectories(Paths.get(resultsDirectory));
        
        // Create JSON configuration
        JSONObject json = new JSONObject();
        json.put("projectId", projectId);
        json.put("name", name);
        json.put("description", description);
        json.put("dataDirectory", "sources");
        json.put("resultsDirectory", "results");
        json.put("createdDate", createdDate.toString());
        json.put("lastModified", new Date().toString());
        
        JSONObject metadataJson = new JSONObject();
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            metadataJson.put(entry.getKey(), entry.getValue());
        }
        json.put("metadata", metadataJson);
        
        // Write to file
        Path configPath = Paths.get(baseDirectory, "project.json");
        Files.writeString(configPath, json.toString(2));
    }

    /**
     * Discover all projects in a directory
     */
    public static List<ProjectConfig> discoverProjects(String projectsDirectory) throws IOException {
        List<ProjectConfig> projects = new ArrayList<>();
        Path projectsDir = Paths.get(projectsDirectory);
        
        if (!Files.exists(projectsDir)) {
            return projects;
        }
        
        Files.list(projectsDir)
            .filter(Files::isDirectory)
            .forEach(projectDir -> {
                try {
                    Path configFile = projectDir.resolve("project.json");
                    if (Files.exists(configFile)) {
                        ProjectConfig config = ProjectConfig.load(projectDir.toString());
                        projects.add(config);
                    }
                } catch (IOException e) {
                    System.err.println("Failed to load project: " + projectDir + " - " + e.getMessage());
                }
            });
        
        return projects;
    }

    /**
     * Create a new project with the standard directory structure
     */
    public static ProjectConfig createNew(String projectsDirectory, String projectId, String name, String description) throws IOException {
        String projectPath = projectsDirectory + "\\" + projectId;
        ProjectConfig config = new ProjectConfig(projectId, name, description, projectPath);
        config.save();
        return config;
    }

    // Getters and setters
    public String getProjectId() { return projectId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getBaseDirectory() { return baseDirectory; }
    public String getDataDirectory() { return dataDirectory; }
    public String getResultsDirectory() { return resultsDirectory; }
    public Map<String, String> getMetadata() { return metadata; }
    
    public void setName(String name) { 
        this.name = name; 
        this.lastModified = new Date();
    }
    
    public void setDescription(String description) { 
        this.description = description; 
        this.lastModified = new Date();
    }
    
    public void addMetadata(String key, String value) {
        this.metadata.put(key, value);
        this.lastModified = new Date();
    }

    @Override
    public String toString() {
        return String.format("Project[%s]: %s - %s", projectId, name, description);
    }
}
