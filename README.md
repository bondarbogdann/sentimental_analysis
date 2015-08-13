This is an app for extracting relevant information from a text(including most of the formats .doc, .pdf, .txt, etc.) and web resources through REST API.

It uses manual implementation of MapReduce principle through Java Concurrency mechanisms, provides full Java Documentation and covered with Unit Tests.

Use cases:
- Web Interface
curl -F "text=some text" http://localhost:8080/text
curl -F "file=@path/to/file.txt(.doc, .pdf, etc.)" http://localhost:8080/file 

- Console Interface
parameters: -input path/to/file.txt(.doc, .pdf, etc.) -url http://some.url