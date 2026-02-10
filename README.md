🚀 Autonomous Insurance Claims Processing Agent

📌 Project Description

The Autonomous Insurance Claims Processing Agent is a Spring Boot backend system that automates the First Notice of Loss (FNOL) insurance claim intake process.

The system:

Extracts key fields from FNOL documents (PDF / TXT)

Detects missing or inconsistent data

Classifies claims

Routes claims to appropriate workflows

Provides reasoning for routing decisions

🧠 Business Problem Solved

Insurance companies manually process FNOL documents which causes:

⏳ Delays

❌ Human errors

💰 Higher operational cost

This agent automates early claim processing and routing decisions.

🏗️ System Architecture
Client / Postman
      ↓
REST Controller
      ↓
Claims Extraction Service (Orchestrator)
      ↓
Document Parsing Service
      ↓
Field Extraction Engine
      ↓
Validation Engine
      ↓
Routing Engine
      ↓
Database (JPA / H2)

⚙️ Tech Stack
Backend

Java 17+

Spring Boot

Spring Data JPA

Hibernate

Document Processing

Apache PDFBox

Database

H2 (Development)

📂 Project Structure
src/main/java/com/synapx/insurance/
│
├── controller/
├── service/
├── repository/
├── model/
└── Application.java

🔄 Processing Workflow
1️⃣ Document Upload

User uploads FNOL document via API.

2️⃣ Document Parsing

PDF → Apache PDFBox Text Extraction

TXT → Direct Processing

3️⃣ Field Extraction

Extracts:

✔ Policy Number
✔ Policy Holder Name
✔ Incident Date & Time
✔ Location
✔ Description
✔ Claimant Details
✔ Asset Details
✔ Estimated Damage
✔ Claim Type

4️⃣ Validation Rules

Checks:

Mandatory field presence

Data consistency

Logical validation

5️⃣ Claim Routing Rules
Rule	Route
Missing mandatory fields	MANUAL_REVIEW
Fraud keywords detected	INVESTIGATION
Claim Type = Injury	SPECIALIST_QUEUE
Damage < 25,000	FAST_TRACK
Otherwise	STANDARD_QUEUE
📤 Output Format
{
  "extractedFields": {},
  "missingFields": [],
  "recommendedRoute": "",
  "reasoning": ""
}

🌐 REST API Endpoints
✅ Process FNOL Claim
POST /api/v1/claims/process

Request
{
  "documentType": "TXT",
  "documentContent": "Base64 or Text Content",
  "fileName": "fnol.txt",
  "documentFormat": "TEXT"
}

❤️ Health Check
GET /api/v1/claims/health

🗄️ Database
H2 Console
http://localhost:8080/h2-console

▶️ Running Locally
🔹 Clone Repo
git clone https://github.com/yourusername/claims-processing-agent.git
cd claims-processing-agent

🔹 Build Project
mvn clean install

🔹 Run Application
mvn spring-boot:run

🧪 Sample CURL Test
curl -X POST http://localhost:8080/api/v1/claims/process \
-H "Content-Type: application/json" \
-d '{
  "documentType": "TXT",
  "documentContent": "FIRST NOTICE OF LOSS...",
  "fileName": "fnol.txt",
  "documentFormat": "TEXT"
}'

🛡️ Error Handling

✔ Centralized Exception Handling
✔ Logging with SLF4J
✔ Graceful DB Failure Handling

🔮 Future Improvements

AI-based extraction using LLMs

Kafka event streaming

Rule engine integration (Drools)

Cloud deployment

Fraud detection ML model

Batch document processing

🧪 Testing Strategy

Unit Tests → Services

Integration Tests → APIs

Sample FNOL Document Tests

⭐ Key Highlights (Assessment Focus)

✔ Real-world Insurance Domain
✔ Clean Layered Architecture
✔ Business Rule Engine
✔ Document Processing
✔ Spring Boot Best Practices
✔ Scalable Design

👨‍💻 Author

Harsha Vardhan
Java Backend Developer | Spring Boot | System Design

📜 License

MIT License
