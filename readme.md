# Task Manager

Task Manager is a web application designed to help users create, manage, and track their tasks efficiently. Users can register, log in, create tasks, set priorities, update statuses, and organize their work effectively.

## Features

- User authentication
- Task creation and management
- Assign priorities to tasks
- Track task status (TODO, IN_PROGRESS, DONE)
- User-specific task management
- Find and filter tasks by status, priority
- RESTful API endpoints for task operations
- A Board of Tasks

## Technologies Used

- **Backend**: Java, Spring Boot, Spring Security, Spring Data JPA
- **Database**: Oracle (non-embedded)
- **Testing**: JUnit, Mockito, Spring Boot Test, MockMvc
- **Build Tool**: Maven

## Installation & Setup

### Prerequisites

- Java 17+
- Maven
- Oracle Database (Configured and running)

### Steps to Run Locally

1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/task-manager.git
   cd task-manager
   ```

2. Configure the database in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
   spring.datasource.username=your_db_user
   spring.datasource.password=your_db_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Build and run the application:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

4. The application should now be running at `http://localhost:8080`.

## API Endpoints

### User Endpoints

- **Register a new user**
  ```http
  POST /users
  ```
- **Get a user**
  ```http
  GET /users/{username}
  ```
- **Update a user**
  ```http
  PUT /users/{username}
  ```
- **Remove a user**
  ```http
  DELETE /users/{username}
  ```


### Task Endpoints

- **Create a new task**
  ```http
  POST /users/{username}/tasks
  ```
- **Get all tasks for a user**
  ```http
  GET /users/{username}/tasks
  ```
- **Find tasks by status**
  ```http
  GET /users/{username}/tasks?status=TODO
  ```
- **Find tasks by priority**
  ```http
  GET /users/{username}/tasks?priority=HIGH
  ```
- **Filter tasks by priority/status**
  ```http
  GET /users/{username}/tasks?sortBy=priority
  ```
- **Update task status**
  ```http
  PUT /users/{username}/tasks/{taskId}/status
  ```

## Running Tests

To run unit and integration tests:
```sh
mvn test
```

