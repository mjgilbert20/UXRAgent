# UXR Agent

A Java-based agent framework for validating research insights, assertions, and claims through traceable attribution and semantic search across multiple source types.

## Overview

The UXR Agent implements a two-phase approach for validating research content against diverse data sources:

**Phase 1: Discovery & Search**
- Semantic search across multiple source collections (interview transcripts, research reports, documents)
- Query reasoning over heterogeneous file repositories 
- Returns ranked candidate insights as structured result sets
- Leverages Azure OpenAI for natural language understanding across different content types

**Phase 2: Verification & Attribution**
- Iterative validation of candidate insights against source materials
- Exact text extraction and matching for provenance tracking across all source types
- Collection of verified matches with source attribution and type classification
- Traceability chain from insight back to original content (interview, report, document, etc.)

## Architecture

### Core Components
- **SemanticMatcher**: LLM-powered semantic similarity detection across source types
- **SourceRepository**: Manages ingestion and indexing of heterogeneous sources (interviews, reports, documents)
- **InsightVerifier**: Validates and traces insight attribution across multiple source types
- **MatchResult**: Structured representation of verification outcomes with source type metadata
- **SourceType**: Enumeration defining supported source categories (TEXT_TRANSCRIPT, RESEARCH_REPORT, DOCUMENT, etc.)

### Data Flow
1. Ingest diverse sources into repository (interviews, reports, documents)
2. Process search queries through semantic matcher
3. Generate candidate insight list with confidence scores and source type awareness
4. Verify each candidate against all applicable source materials
5. Extract matching text segments for attribution with source type classification
6. Build verified insight collection with full traceability and source diversity metrics

## Project Structure

```
isThisInHere/
├── src/                # Core Java implementation
│   ├── Main.java       # Entry point and orchestration
│   ├── SemanticMatcher.java    # LLM-based matching engine
│   ├── SourceRepository.java   # Multi-source data ingestion and management
│   ├── InsightVerifier.java    # Verification and attribution across source types
│   ├── MatchResult.java        # Result data structures with source metadata
│   ├── SourceType.java         # Source type enumeration and classification
│   ├── ProjectConfig.java      # Project configuration and management
│   ├── ProjectManager.java     # Multi-project handling and discovery
│   └── [Source interface classes] # Polymorphic data models (TextTranscript, ResearchReport, etc.)
├── design/             # Technical specifications
├── lib/                # Dependencies (org.json, etc.)
├── projects/           # Project workspace (organized by project)
│   ├── banking_study_2024/     # Example project
│   │   ├── project.json        # Project configuration
│   │   ├── sources/            # Project-specific sources
│   │   │   ├── interviews/
│   │   │   ├── reports/
│   │   │   └── documents/
│   │   └── results/            # Analysis outputs
│   └── mobile_app_research/    # Another project
├── testData/           # Legacy test data (for backward compatibility)
├── out/                # Compiled artifacts (gitignored)
```

## Requirements

- Java 17 or newer (Java 21 recommended)
- [Azure OpenAI Service](https://azure.microsoft.com/en-us/products/ai-services/openai-service) API key
- `json-20240303.jar` (included in `lib/`)

## Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/your-friend/UXRAgent.git
   cd UXRAgent
   ```

2. **Set your Azure OpenAI API key as an environment variable:**
   - On Windows (PowerShell):
     ```powershell
     $env:OPENAI_API_KEY="your-azure-openai-key"
     ```
   - On Mac/Linux (bash):
     ```bash
     export OPENAI_API_KEY="your-azure-openai-key"
     ```

3. **Compile the project:**
   ```sh
   javac -cp isThisInHere/lib/json-20240303.jar -d isThisInHere/out isThisInHere/src/*.java
   ```

4. **Run with project management:**

   **Option A: Run specific project**
   ```sh
   java -cp "isThisInHere/lib/json-20240303.jar;isThisInHere/out" Main multi-source --project "projects/banking_study_2024"
   ```

   **Option B: Auto-discover projects**
   ```sh
   java -cp "isThisInHere/lib/json-20240303.jar;isThisInHere/out" Main multi-source
   ```

   **Option C: Legacy mode (testData directory)**
   ```sh
   java -cp "isThisInHere/lib/json-20240303.jar;isThisInHere/out" Main
   ```

## Project Management

The UXR Agent now supports **multi-project workspaces** with automatic project discovery and isolation:

### Creating a New Project

Projects are organized in the `projects/` directory with this structure:
```
projects/
├── your_project_id/
│   ├── project.json          # Project configuration
│   ├── sources/              # Project-specific sources
│   │   ├── interviews/       # Interview transcripts (.txt)
│   │   ├── reports/          # Research reports (.txt)
│   │   └── documents/        # Documents and specifications (.txt)
│   └── results/              # Analysis outputs
```

### Project Configuration (project.json)
```json
{
  "projectId": "your_project_id",
  "name": "Your Project Name",
  "description": "Brief description of your research project",
  "dataDirectory": "sources",
  "resultsDirectory": "results",
  "metadata": {
    "researcher": "Your Name",
    "institution": "Your Institution",
    "startDate": "YYYY-MM-DD",
    "methodology": "Your methodology"
  }
}
```

### Benefits of Project Management
- **Isolation**: Each project maintains separate sources and results
- **Organization**: Clear project boundaries and metadata
- **Traceability**: Results are saved to project-specific directories
- **Discovery**: Automatic detection of available projects
- **Scalability**: Support for multiple concurrent research projects

## Notes

- The project uses Azure OpenAI for semantic operations. You must have a valid API key.
- Do **not** commit your API key to the repository.
- All compiled `.class` files and IDE config folders are excluded from version control via `.gitignore`.

## License

MIT License (or specify your license here)
