import java.io.IOException;
import java.util.*;

/**
 * Manager class for handling multiple research projects
 * Provides project discovery, selection, and management capabilities
 */
public class ProjectManager {
    private String projectsBaseDirectory;
    private Map<String, ProjectConfig> loadedProjects;
    
    public ProjectManager(String projectsBaseDirectory) {
        this.projectsBaseDirectory = projectsBaseDirectory;
        this.loadedProjects = new HashMap<>();
    }
    
    /**
     * Discover and load all available projects
     */
    public List<ProjectConfig> discoverProjects() throws IOException {
        List<ProjectConfig> projects = ProjectConfig.discoverProjects(projectsBaseDirectory);
        
        // Cache loaded projects
        loadedProjects.clear();
        for (ProjectConfig project : projects) {
            loadedProjects.put(project.getProjectId(), project);
        }
        
        return projects;
    }
    
    /**
     * Get a specific project by ID
     */
    public ProjectConfig getProject(String projectId) {
        return loadedProjects.get(projectId);
    }
    
    /**
     * Create a new project
     */
    public ProjectConfig createProject(String projectId, String name, String description) throws IOException {
        ProjectConfig project = ProjectConfig.createNew(projectsBaseDirectory, projectId, name, description);
        loadedProjects.put(projectId, project);
        return project;
    }
    
    /**
     * Display interactive project selection menu
     */
    public ProjectConfig selectProject() throws IOException {
        List<ProjectConfig> projects = discoverProjects();
        
        if (projects.isEmpty()) {
            System.out.println("🔍 No projects found in: " + projectsBaseDirectory);
            System.out.println("💡 Create a new project or check your projects directory.");
            return null;
        }
        
        System.out.println("📁 AVAILABLE PROJECTS");
        System.out.println("════════════════════");
        
        for (int i = 0; i < projects.size(); i++) {
            ProjectConfig project = projects.get(i);
            System.out.printf("%d. %s\n", i + 1, project.getName());
            System.out.printf("   ID: %s\n", project.getProjectId());
            System.out.printf("   Description: %s\n", project.getDescription());
            System.out.println();
        }
        
        return projects.get(0); // For demo, return first project
    }
    
    /**
     * Display project statistics
     */
    public void displayProjectStats(ProjectConfig project) throws IOException {
        System.out.println("📊 PROJECT: " + project.getName());
        System.out.println("─────────────────────");
        System.out.println("ID: " + project.getProjectId());
        System.out.println("Description: " + project.getDescription());
        System.out.println("Data Directory: " + project.getDataDirectory());
        System.out.println("Results Directory: " + project.getResultsDirectory());
        
        // Count sources if directory exists
        try {
            SourceRepository repository = SourceLoader.loadAllSources(project.getDataDirectory());
            System.out.println("Total Sources: " + repository.getAllSources().size());
            
            for (SourceType type : SourceType.values()) {
                int count = repository.getSourcesByType(type).size();
                if (count > 0) {
                    System.out.printf("  %s: %d sources\n", type, count);
                }
            }
        } catch (IOException e) {
            System.out.println("No sources loaded yet.");
        }
        
        System.out.println();
    }
}
