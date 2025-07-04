# Product Requirements Document (PRD)

## Project: UXR Agent MVP

---

## 1. Problem Statement

Modern UX research workflows require not only the discovery of insights but also robust **attribution** and **traceability**—the ability to show exactly where an insight came from, and to verify its presence in source data. This is foundational for research rigor, transparency, and trust in findings.

---

## 2. Key Concepts: SemanticMatcher vs. InsightVerifier

A core architectural distinction in this project is between the `SemanticMatcher` and the `InsightVerifier` classes:

- **SemanticMatcher**
    - Compares a single insight (string) to a single source (string, e.g., a transcript).
    - Answers: *"Is this in here?"* for one source at a time.
    - Foundational for attribution: it provides the atomic, traceable answer for a specific source.

- **InsightVerifier**
    - Orchestrates checking an insight against multiple sources (e.g., all transcripts).
    - Answers: *"Is this in any of these?"* by using `SemanticMatcher` for each source.
    - Foundational for traceability: it enables batch verification and reporting across a corpus.

**Why this matters:**
- Attribution and traceability are not just features—they are the backbone of defensible, auditable UX research.
- The architecture ensures every insight can be traced back to its supporting evidence, or lack thereof, in the data.

---

## 3. Goals & Scope

- Support ingestion of multiple transcript files as sources.
- Enable semantic matching of insights against one or many sources.
- Provide clear, auditable output showing which sources support which insights.
- Lay groundwork for future extensibility (e.g., new source types, richer validation, job map automation).

---

## 4. Out of Scope (MVP)

- Automated research planning, lit review, or synthesis.
- Support for non-text sources (e.g., video, survey) beyond stubs.
- UI/UX beyond basic file and console output.

---

## 5. Success Criteria

- Given an insight and a set of transcript files, the system can:
    - Rephrase the insight (LLM)
    - Check for its presence in each transcript (LLM-powered semantic match)
    - Output which sources support the insight, with traceable evidence

---

## 6. Future Directions

- Add support for more source types (e.g., video, survey, web).
- Automate more steps in the research job map.
- Enhance validation and explainability of matches.
- Integrate with Model Context Protocol (MCP) and other AI research standards.
- Access management (to the different content elements)
- Data governance, eg. residency and expiration

---

## 7. References

- See `CLASS_DIAGRAM.md` and `OBJECT_DIAGRAM.md` for architecture.
- See `ROUGH_NOTES.md` for ideation and open questions.
