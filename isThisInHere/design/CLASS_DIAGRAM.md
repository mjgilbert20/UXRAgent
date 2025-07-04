# Class Diagram

```
+-------------------+
| InterviewSource   |  <<abstract>>
+-------------------+
| id: UUID          |
| metadata: Map     |
+-------------------+
| getContent(): String
| getType(): SourceType
+-------------------+
         ^
         |
+-------------------+
| TextTranscript    |
+-------------------+
| filePath: String  |
| content: String   |
+-------------------+
| getContent()      |
| getType()         |
+-------------------+

+-----------------------------+
| SemanticMatcher             |
+-----------------------------+
| match(thisStr, hereStr):    |
|   MatchResult               |
+-----------------------------+

+-----------------------------+
| MatchResult                 |
+-----------------------------+
| isMatch: boolean            |
| confidence: double          |
| matchedStrings: List<String>|
+-----------------------------+

+-----------------------------+
| InsightVerifier             |
+-----------------------------+
| matcher: SemanticMatcher    |
+-----------------------------+
| verify(thisStr, sources):   |
|   List<VerificationResult>  |
+-----------------------------+

+-----------------------------+
| VerificationResult          |
+-----------------------------+
| source: InterviewSource     |
| matchResult: MatchResult    |
+-----------------------------+
```

This diagram represents the core classes and relationships for the MVP. Extend as needed for future features.
