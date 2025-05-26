# Sistema de Controle de Escalas Semanais - Backend

## Descrição
API REST desenvolvida em Java com Spring Boot para cadastro e controle de funcionários, escalas semanais e turnos de trabalho (WorkHours).

---

## Endpoints

### `/employees`
- `GET /employees`: Lista todos os funcionários
- `POST /employees`: Cadastra um novo funcionário
- `PUT /employees/{id}`: Atualiza os dados de um funcionário existente
- `DELETE /employees/{id}`: Remove um funcionário

---

### `/roster`
- `GET /roster`: Lista todas as escalas semanais (WeekRosters)
- `POST /roster`: Cria uma nova escala
- `PUT /roster/{id}`: Atualiza uma escala existente
- `DELETE /roster/{id}`: Remove uma escala

---

### `/shifts`
- `GET /shifts`: Lista todos os turnos (WorkHours)
- `POST /shifts`: Cadastra um turno individual
- `POST /shifts/schedule`: Cadastra múltiplos turnos da semana para um ou mais funcionários
- `PUT /shifts/{id}`: Atualiza um turno específico
- `DELETE /shifts/{id}`: Remove um turno

---

## Regras e Observações
- Quando `dayOff = true`, os campos `startHour` e `finishHour` são ignorados.
- A validação impede que `startHour` seja posterior a `finishHour`.
- O endpoint `/shifts/schedule` aceita uma lista de objetos no seguinte formato:

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

## Tecnologias utilizadas
- Java 17
- Spring Boot
- Spring Data JPA
- Banco de Dados (H2, PostgreSQL, etc.)
- Gradle (via `gradlew`)