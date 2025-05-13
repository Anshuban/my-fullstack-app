
# Full-Stack Shift Scheduler

A full-stack application to generate shift schedules for employees based on the input data. The backend is built using Spring Boot, and the frontend is built using React. The application communicates between the frontend and backend via HTTP requests to generate and display schedules.

## Technologies Used

- **Frontend:**  HTML, CSS, JavaScript
- **Backend:** Spring Boot (Java)
- **Database (if applicable):** None for this example, but can be added for persistence
- **Tools:** Postman, npm, Maven

## Setup

### 1. Backend Setup

The backend is built using **Spring Boot**. To set up the backend:

1. Clone the repository:
   ```bash
   git clone <repo-url>


2. Navigate to the backend folder:

   ```bash
   cd backend
   ```

3. Make sure you have JDK 11 or higher installed.

4. Run the Spring Boot application using Maven:

   ```bash
   mvn spring-boot:run
   ```

   By default, the backend will be accessible at [http://localhost:8080](http://localhost:8080).

5. Ensure that your backend is correctly handling CORS requests (from the frontend running on port 3000).

### 2. Frontend Setup

The frontend is built using React. To set up the frontend:

1. Navigate to the frontend folder:

   ```bash
   cd frontend
   ```

2. Install the necessary dependencies:

   ```bash
   npm install
   ```

3. Start the frontend development server:

   ```bash
   npm start
   ```

   The frontend will be accessible at [http://localhost:3000](http://localhost:3000).

### 3. Accessing the Application

* **Backend (API):** The backend is accessible at [http://localhost:8080/api/scheduler](http://localhost:8080/api/scheduler).
* **Frontend (User Interface):** The frontend is accessible at [http://localhost:3000](http://localhost:3000).

### 4. Generating the Schedule

1. Open the frontend at [http://localhost:3000](http://localhost:3000).
2. Input the total number of employees and general employees in the form.
3. Click "Generate Schedule" to receive the generated shift schedule.

### 5. Testing the Backend API

You can test the backend API using Postman or curl.

#### Sample POST Request to Generate Schedule

* **URL:** [http://localhost:8080/api/scheduler/generate](http://localhost:8080/api/scheduler/generate)
* **Method:** POST
* **Body (JSON):**

  ```json
  {
    "totalEmployees": 10,
    "generalEmployees": 5
  }
  ```
* **Response:**

  ```json
  {
    "normalSchedule": [
      {
        "Day": "Monday",
        "Employee 1": "Shift 1",
        "Employee 2": "Shift 2"
      },
      {
        "Day": "Tuesday",
        "Employee 1": "Shift 2",
        "Employee 2": "Shift 1"
      }
    ],
    "generalSchedule": [
      {
        "Day": "Monday",
        "Employee 3": "Shift 1",
        "Employee 4": "Shift 2"
      },
      {
        "Day": "Tuesday",
        "Employee 3": "Shift 2",
        "Employee 4": "Shift 1"
      }
    ]
  }
  ```

### 6. Troubleshooting

* If you encounter CORS errors, ensure that your backend has proper CORS configuration allowing requests from [http://localhost:3000](http://localhost:3000).
* Make sure both the frontend and backend are running on their respective ports (3000 for the frontend and 8080 for the backend).

### 7. Future Improvements

* **Authentication:** Implement user authentication and authorization for shift management.
* **Database Integration:** Add a database to store employee schedules persistently.
