# EasyShift

Backend system built with Java and Spring Boot to manage weekly employee shift schedules.

---

## Project Description

This project was born out of a real-world need identified by my manager, who struggled to manage weekly staff schedules
using either paid applications or manually on paper (A4 sheets) to avoid subscription costs. As a backend developer, 
I decided to create a web-based system to help solve her problem in a more efficient and organized way. This initiative
not only aims to provide a free and practical solution for her daily operations, but also serves as a valuable 
opportunity for me to gain hands-on experience in developing and delivering real-world software solutions.

## 🚀 Technologies Used

- Java 17+
- Spring Boot
- Spring Data JPA
- H2 Database (or any other relational database)
- Gradle

---

## ✅ Features

- Register and manage employees
- Create and assign weekly rosters
- Schedule working hours (start, finish, day off) per day
- Validate shift rules
- API integration ready for frontend use

---

## 🛠️ How to Run

```bash
./gradlew bootRun
```

Then access the app at:

```
http://localhost:8080
```

---

## 📁 Folder Structure

```
src/
 └── main/
     ├── java/com/MyProject/...   # Source code
     └── resources/
         ├── application.properties
         └── static/templates/... # (If using web resources)
```

---

## 📌 API Overview (sample)

- `POST /schedule` – Submit a full weekly schedule
- `GET /employees` – List all employees
- `POST /roster` – Create a new weekly roster

> JSON examples and detailed documentation will be added as development continues.

---

## 🔒 License

This project is for internal use by the lanchonete team.
