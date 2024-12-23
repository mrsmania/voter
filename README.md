# Voter

This application allows users to add Questions and Answers, and then vote on them.

## Dev-Stack:
- frontend: Angular
- backend: Spring Boot
- database: H2

## Helping Tools:
- postman (for testing the REST API): https://www.postman.com/downloads/
- angular CLI (for running the Angular application): https://angular.io/cli

## Features:
- add Questions
- add Answers to Questions
- vote on Answers
- view Questions and Answers

## Installation:
1. clone the repository
2. open the project in your favorite IDE
3. set your preferred profile, dev or prod in the application.properties

### Backend:
in a new Terminal
1. (if not already) npm install -g @angular/cli
 
### Frontend:
in a new Terminal in the root folder:
1. cd frontend
2. npm install


## How to run:
### Backend:
1. run the Spring Boot application (main method in the VoterApplication class)
2. the application will be available at `http://localhost:8080`
3. if prod profile set
   1. DB Console: `http://localhost:8080/h2-console`
   2. JDBC URL: `jdbc:h2:file:./data/test`
   3. User Name: `sa`
   4. Password: `password`
4. if dev profile set
   1. DB Console: `http://localhost:8080/h2-console`
   2. JDBC URL: `jdbc:h2:mem:testdb`
   3. User Name: `sa`
   4. Password: [no password]

### Frontend local machine:
1. cd frontend
2. ng serve
3. open the browser and navigate to `http://localhost:4200`

### Frontend other machine:
1. cd frontend on local machine
2. ng serve --host 0.0.0.0 --port 4200
3. open the browser of the other machine and access the application at the IP found in the console (IP of the local machine)