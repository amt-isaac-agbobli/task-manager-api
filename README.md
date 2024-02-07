# Task Manager API

The Task Manager API is a Spring Boot application developed in Java, using Maven as the build tool. This API provides a set of endpoints to manage tasks in a task management system.

## Features

- **User Authentication:** The API provides endpoints for user registration and login. It uses JSON Web Tokens (JWT) for authentication.

- **Task Management:** The API allows users to create, retrieve, update,assign, reassign and delete tasks. Each task has properties like title, description, status, and due date.

- **Caching:** The API uses Ehcache for caching. Caching improves the performance of the API by storing frequently accessed data in memory, reducing the need for database calls.

- **Pagination:** The API supports pagination on the task retrieval endpoint. This allows clients to retrieve tasks in smaller chunks rather than all at once, which can be beneficial for performance when dealing with large amounts of data.

- **Email Notifications:** The API sends email notifications to users when certain events occur, such as task creation or completion, assigning or reassigning task and resetting of user password.

- **Deodorization:** The application is containerized using Docker, which makes it easy to build, ship, and run on any platform that supports Docker.

## Technologies

- Java
- Spring Boot
- Maven
- Docker
- Ehcache
- PostgresSQL
- JWT for Authentication
- Javax Mail for Email Notifications

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java 8 or higher
- Maven
- Docker

### Installing

1. Clone the repository
```bash
git clone https://github.com/amt-isaac-agbobli/task-manager-api.git
```

2. Navigate to the project directory
```bash
cd task-manager-api
```

3. Build the project with Maven
```bash
mvn clean install
```

4. Build the Docker image
```bash
docker build -t task-manager-api .
```

5. Run the Docker container
```bash
docker run -p 8080:8080 task-manager-api
```

The application will be accessible at `http://localhost:8080`.

## API Endpoints

The Task Manager API provides the following endpoints:

- `POST /api/v1/users/register`: Register a new user
- `POST /api/v1/users/login`: Login a user
- `POST /api/v1/tasks`: Create a new task
- `GET /api/v1/tasks`: Retrieve all tasks with pagination
- `GET /api/v1/tasks/{id}`: Retrieve a task by ID
- `PUT /api/v1/tasks/{id}`: Update a task by ID
- `DELETE /api/v1/tasks/{id}`: Delete a task by ID

## Running the tests

To run the tests, use the following Maven command:

```bash
mvn test
```

## Deployment

This application is containerized using Docker, and can be easily deployed on any platform that supports Docker.

