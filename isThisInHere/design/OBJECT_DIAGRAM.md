# Object Diagram: Main.java (Runtime Example)

```
+-------------------+
| apiKey            |
+-------------------+
| String            |
| (OpenAI API key)  |
+-------------------+

+-------------------+
| baseDir           |
+-------------------+
| String            |
| (Transcript dir)  |
+-------------------+

+-------------------+
| filenames         |
+-------------------+
| String[]          |
| ["interview1.txt", ...] |
+-------------------+

+-------------------------------+
| transcripts                   |
+-------------------------------+
| Map<String, String>           |
| filename -> file content      |
+-------------------------------+

+-------------------------------+
| sources                       |
+-------------------------------+
| List<InterviewSource>         |
| [TextTranscript, ...]         |
+-------------------------------+
        |
        v
+-------------------------------+
| TextTranscript                |
+-------------------------------+
| id: UUID                      |
| filePath: String              |
| content: String               |
| metadata: Map                 |
+-------------------------------+

+-------------------+
| insight           |
+-------------------+
| String            |
| (Insight string)  |
+-------------------+

+-------------------+
| output            |
+-------------------+
| StringBuilder     |
| (Output text)     |
+-------------------+

+-------------------+
| rephrasedInsight  |
+-------------------+
| String            |
| (LLM rephrased)   |
+-------------------+
```

**Note:**
- Helper objects like `HttpClient`, `HttpRequest`, `HttpResponse`, `JSONObject`, and `JSONArray` are created as needed in methods and are not shown here.
- This diagram shows the main runtime objects and their relationships for the current script logic.
