# Pastoral Visit Manager 2.5

Trello (TODO tasks): https://trello.com/b/RYXTHF8z/pastoral-visit-manager

### 1. Requirements
1. Java 8+
2. Maven 3.5+
3. Node 8.6.0+ https://nodejs.org/en/
4. Npm 5.3.0+
5. Angular Cli 1.6.1+
6. PostgreSQL 10

### 2. Preparation

Maven: https://maven.apache.org/download.cgi

Node and npm: https://nodejs.org/en/

Angular Cli:
1. Open terminal anywhere
2. Run: npm install -g @angular/cli

PostgreSQL Server:
1. Download and install postgres server
2. Go to Task Manager > Services > find postgres-x64-10 > Run (if not runnning)

### 3. Installation
1. Clone the repository
2. Import project into InteliJ IDEA (from existing sources > as Maven > next, next)
3. Find PastoralVisitManagerApplication class and run it
4. Backend server is running at localhost:8090
5. Go to frontend folder
6. Open terminal there
7. Execute: 
        
        npm install 
8. Than run: 

        ng serve --open
9. Frontend is listening at localhost:4200 (--open option will open the browser after compilation)
