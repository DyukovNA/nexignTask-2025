# NexignTask-2025

## Описание проекта
Проект CDR Service представляет собой микросервис для генерации и обработки Call Detail Records (CDR) — записей о звонках абонентов сотового оператора. Сервис генерирует CDR записи, сохраняет их в базу данных и предоставляет REST API для получения отчетов в формате UDR (Usage Data Record).

### Основные функции:

- Генерация CDR записей для абонентов.
- Сохранение CDR записей в базу данных.
- Предоставление REST API для получения UDR отчетов.

### REST Endpoints

1. Получение UDR отчета для одного абонента
   
   Метод: GET
   URL: /udr/{msisdn}

   Параметры:
   - msisdn (строка): Номер абонента.
   - month (опционально, строка): Месяц в формате YYYY-MM.

   Пример запроса №1:
   "http://localhost:8080/udr/79992221122"

   Ответ:
   {
     "msisdn": "79992221122",
     "incomingCall": {
       "totalTime": "179:12:44"
     },
     "outcomingCall": {
       "totalTime": "187:01:02"
     }
   }
   
   Пример запроса №2:
   "http://localhost:8080/udr/79992221122?month=2025-02"
   
   Ответ:
   {
     "msisdn": "79992221122",
     "incomingCall": {
       "totalTime": "15:27:44"
     },
     "outcomingCall": {
       "totalTime": "13:33:42"
     }
   }
   
2. Получение UDR отчетов для всех абонентов за месяц
   Метод: GET

   URL: /udr/monthly
   
   Параметры:
   
   month (строка): Месяц в формате YYYY-MM.
   
   Пример запроса:
   
   "http://localhost:8080//udr/monthly?month=2025-02"
   
   Ответ:
   
   {
      "79992221122": {
         "msisdn": "79992221122",
         "incomingCall": {
            "totalTime": "02:12:13"
         },
         "outcomingCall": {
            "totalTime": "00:02:50"
         }
      },
      "79993331133": {
         "msisdn": "79993331133",
         "incomingCall": {
            "totalTime": "01:30:00"
         },
         "outcomingCall": {
            "totalTime": "00:45:00"
         }
      }
   }

3. Генерация CDR отчета

   Метод: POST
   URL: /report/generate

   Параметры:
   - msisdn (строка): Номер абонента.
   - startDate (дата и время): Начало периода.
   - endDate (дата и время): Конец периода.
   
   Пример запроса:
   "http://localhost:8080/report/generate?msisdn=79992221122&startDate=2025-02-01T00:00:00&endDate=2025-02-28T23:59:59"

   Ответ: "Request ID: 6904e40f-43cb-4947-acc1-d1fbcea2f724"

   Пример запроса с ошибкой:
   "http://localhost:8080/report/generate?msisdn=79992221122&startDate=2023-02-01T00:00:00&endDate=2023-02-28T23:59:59"
   
   Ответ: "Error: No CDR records found"

   
### Библиотеки

   1. Spring Boot. Упрощает разработку и настройку приложения.
   
   2. Spring Data JPA. Упрощает взаимодействие с базой данных, предоставляет готовые методы для CRUD-операций и кастомных запросов.
   
   3. H2 Database. Используется для тестирования и локальной разработки, не требует установки внешней СУБД.
   
   4. Lombok. Уменьшает объем кода, делает его более читаемым и поддерживаемым.
   
   5. JUnit 5. Необходим для написания тестов. 
   
   6. Mockito. Позволяет изолировать тестируемый код от зависимостей, упрощает тестирование.
   
   7. Spring Boot Starter Web. Предоставляет инструменты для создания REST API и обработки HTTP-запросов.
   
   8. JaCoCo. Используется для анализа покрытия кода тестами.

### Покрытие тестами

<img width="594" alt="Снимок экрана 2025-03-23 в 14 23 55" src="https://github.com/user-attachments/assets/fe543d16-67ea-4b4f-8c88-ea1a0d0749be" />


   mvn clean test jacoco:report

