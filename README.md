# UXR Agent

A Java-based agent framework for supporting UX Research insight validation through traceable attribution.

## Features

- Ingests multiple interview transcripts.
- Uses Azure OpenAI for semantic matching and rephrasing.
- Modular design for future extension (e.g., additional source types).

## Project Structure

```
isThisInHere/
├── src/                # Java source files
├── design/             # Design docs (class/object diagrams, PRD, notes)
├── lib/                # External libraries (e.g., json-20240303.jar)
├── isThisInHereTestFiles/ # Example transcripts and test outputs
├── out/                # Compiled .class files (not tracked in git)
```

## Requirements

- Java 17 or newer (Java 21 recommended)
- [Azure OpenAI Service](https://azure.microsoft.com/en-us/products/ai-services/openai-service) API key
- `json-20240303.jar` (included in `lib/`)

## Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/your-friend/uxrAgent.git
   cd uxrAgent
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
   javac -cp isThisInHere/lib/json-20240303.jar isThisInHere/src/*.java
   ```

4. **Run the main program:**
   ```sh
   java -cp "isThisInHere/lib/json-20240303.jar;isThisInHere/src" Main
   ```

## Notes

- The project uses Azure OpenAI for semantic operations. You must have a valid API key.
- Do **not** commit your API key to the repository.
- All compiled `.class` files and IDE config folders are excluded from version control via `.gitignore`.

## License

MIT License (or specify your license here)
