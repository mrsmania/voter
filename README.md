# Voter

This application allows users to add Questions and Answers, and then vote on them.

## Dev-Stack:
- Frontend: Angular
- Backend: Spring Boot
- Database: H2

## Helping Tools:
- Postman (for testing the REST API): https://www.postman.com/downloads/
- Angular CLI (for running the Angular application): https://angular.io/cli

## Features:
- Add Questions
- Add Answers to Questions
- Vote on Answers
- View Questions and Answers

## Installation:
1. Clone the repository
2. Open the project in your favorite IDE

### Backend:
in a new Terminal
1. (if not already) npm install -g @angular/cli
 
### Frontend:
in a new Terminal in the root folder:
1. cd frontend
2. npm install
3. ng serve

## How to run:
### Backend:
1. Run the Spring Boot application
2. The application will be available at `http://localhost:8080`
3. DB Console: `http://localhost:8080/h2-console`
   * JDBC URL: `jdbc:h2:file:./data/test`
   * User Name: `sa`
   * Password: `password`

### Frontend:
1. cd frontend
2. ng serve
3. The application will be available at `http://localhost:4200`
4. Open the browser and navigate to `http://localhost:4200`



