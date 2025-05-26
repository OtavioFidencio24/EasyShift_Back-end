# Weekly Work Schedule Management System - Backend

## Description
RESTful API built with Java and Spring Boot for registering and managing employees, weekly rosters, and work shifts (WorkHours).

---

## Endpoints

### `/employees`
- `GET /employees`: Lists all employees
- `POST /employees`: Creates a new employee
- `PUT /employees/{id}`: Updates an existing employee's data
- `DELETE /employees/{id}`: Deletes an employee

---

### `/roster`
- `GET /roster`: Lists all weekly rosters
- `GET /roster/{id}`: get a specific roster by its ID
- `POST /roster`: Creates a new roster
- `PUT /roster/{id}`: Updates a specific roster
- `DELETE /roster/{id}`: Deletes a roster

---

### `/shifts`
- `GET /shifts`: Lists all work shifts (WorkHours)
- `GET /shifts/byRoster/{id}`: List all workHours associated with a specific roster 
- `POST /shifts`: Creates an individual shift
- `POST /shifts/schedule`: Saves weekly schedules for one or more employees
- `PUT /shifts/{id}`: Updates a specific shift
- `DELETE /shifts/{id}`: Deletes a shift

---

## Rules and Notes
- If `dayOff = true`, the `startHour` and `finishHour` fields are ignored.
- Validation ensures `startHour` must be earlier than `finishHour`.
- The `/shifts/schedule` endpoint accepts a list of objects in the following format:

```json
[
  {
    "employeeId": 1,
    "rosterId": 2,
    "week": [
      {
        "weekDay": "MONDAY",
        "startHour": "09:00",
        "finishHour": "17:00",
        "dayOff": false
      }
    ]
  }
]
```

---

## Technologies Used
- Java 17
- Spring Boot
- Spring Data JPA
- Database NEON (PostgreSQL Cloud)
- Gradle (via `gradlew`)[API_Documentation_EN.md](API_Documentation_EN.md)