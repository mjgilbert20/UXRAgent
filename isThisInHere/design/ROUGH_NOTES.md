# UX Research Study agent(s)

- Planning (from the business process perspective, not the engineering perspective)
- Understanding the problem space, attribution and traceability
- Job map for core job of conducting UXR study
- Study goal/scope/RQs (research questions)
    - User inputs String(s)
- Lit review (Users, Jobs, Tools)
    - User inputs URLS and maybe files → past insights & hypotheses
- Research planning
    - Discussion guide (could also be survey or structure to support other methods)
- Data collection
    - Interview transcripts (could also be other artifacts)
- Synthesis
    - (This is a key area of focus where cross contamination, i.e. semantic blending, is prevented)
- Here is an idea for a validation function: Smallest logical granular component is a “is this in here” function that takes in two arguments, this and here, where this is the insight meaning semantic concept in the form of a string and here is the source where the insight may or may not exist. The function should return yes, no, and maybe with the yes case appending the location in the source or text from the source, and the maybe case also returns this location info.
- Open questions:
    - Are there standards for the sizes of semantic chunks? Can and should different sizes be treated differently? I know a token is a word or part of a word, are there larger standardized chunks? Sentence level, paragraph level, chapter level, book level, for example?
    - Are there standards for context around “this”?
- Sharing results & making an impact

# Mix of biz and dev planning: Identify steps that would be good to automate/make agentic

- Planning (from the engineering perspective) PRD product requirements document
- Logistics
- GitHub & ownership
- Personal PC as sandbox potentially transitioning to work Mac
- Azure AI services
- Skills categories (stuff I could focus on for learning goals)
    - Programming skills (143 prep)
    - Core logic/math/working memory skills (maybe use khan academy)
    - PC/app/environment/security skills

# AI research POC (probably way to complex, but a fascinating problem space)

- Unit testing idea for verifying output to show the problem of defining the gold standard in probabilistic situations where there is some error tolerance, but the patterns in errors could point to security issues such as model contamination.

# Resources

- https://www.anthropic.com/news/model-context-protocol
- https://apidog.com/blog/mcp-vs-api/
- https://smithery.ai
- https://github.com/roboco-io/awesome-vibecoding
- https://youtu.be/pznW9fN-3Qg
- https://modelcontextprotocol.io/introduction
- Streamable HTTP is the new (last month or so) standard Transports - Model Context Protocol
- modelcontextprotocol/python-sdk: The official Python SDK for Model Context Protocol servers and clients
- modelcontextprotocol/servers: Model Context Protocol Servers
- microsoft/mcp: Catalog of official Microsoft MCP (Model Context Protocol) server implementations for AI-powered data access and tool integration
- Kickstart Your AI Development with the Model Context Protocol (MCP) Course | Microsoft Community Hub - Link to a GitHub repo
- YouTube videos
    - MCP vs API: Simplifying AI Agent Integration with External Data
    - How to Build an MCP Client GUI with Streamlit and FastAPI
    - MCP Crash Course for Python Developers
