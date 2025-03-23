# NexignTask-2025

## Описание проекта
Проект CDR Service представляет собой микросервис для генерации и обработки Call Detail Records (CDR) — записей о звонках абонентов сотового оператора. Сервис генерирует CDR записи, сохраняет их в базу данных и предоставляет REST API для получения отчетов в формате UDR (Usage Data Record).

### Основные функции:

- Генерация CDR записей для абонентов.
- Сохранение CDR записей в базу данных.
- Предоставление REST API для получения UDR отчетов.

### REST Endpoints
1. Генерация CDR отчета

   Метод: POST
   URL: /report/generate

   Параметры:
   - msisdn (строка): Номер абонента.
   - startDate (дата и время): Начало периода.
   - endDate (дата и время): Конец периода.
   
   Пример запроса:
   "http://localhost:8080/report/generate?msisdn=79992221122&startDate=2025-02-01T00:00:00&endDate=2025-02-28T23:59:59"

   Ответ: "Request ID: 6904e40f-43cb-4947-acc1-d1fbcea2f724"
   
3. Получение UDR отчета для одного абонента
   
   Метод: GET
   URL: /udr/{msisdn}

   Параметры:
   - msisdn (строка): Номер абонента.
   - month (опционально, строка): Месяц в формате YYYY-MM.

   Пример запроса:
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
   
3. Получение UDR отчетов для всех абонентов за месяц
   Метод: GET

URL: /udr/monthly

Параметры:

month (строка): Месяц в формате YYYY-MM.

Пример запроса:

Copy
GET /udr/monthly?month=2025-02
Ответ:

json
Copy
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
Используемые библиотеки и их обоснование
1. Spring Boot
   Описание: Фреймворк для создания Spring-приложений с минимальной конфигурацией.

Обоснование: Упрощает разработку и настройку приложения, предоставляет встроенные инструменты для работы с REST API, базой данных и тестированием.

2. Spring Data JPA
   Описание: Библиотека для работы с базами данных через JPA (Java Persistence API).

Обоснование: Упрощает взаимодействие с базой данных, предоставляет готовые методы для CRUD-операций и кастомных запросов.

3. H2 Database
   Описание: Встраиваемая база данных, работающая в памяти.

Обоснование: Используется для тестирования и локальной разработки, не требует установки внешней СУБД.

4. Lombok
   Описание: Библиотека для автоматической генерации boilerplate-кода (геттеры, сеттеры, конструкторы и т.д.).

Обоснование: Уменьшает объем кода, делает его более читаемым и поддерживаемым.

5. JUnit 5
   Описание: Фреймворк для написания и запуска unit-тестов.

Обоснование: Обеспечивает надежность и стабильность приложения через автоматическое тестирование.

6. Mockito
   Описание: Библиотека для создания мок-объектов в тестах.

Обоснование: Позволяет изолировать тестируемый код от зависимостей, упрощает тестирование.

7. Spring Boot Starter Web
   Описание: Библиотека для создания веб-приложений с использованием Spring MVC.

Обоснование: Предоставляет инструменты для создания REST API и обработки HTTP-запросов.

Запуск проекта
Клонируйте репозиторий:

bash
Copy
git clone https://github.com/your-username/cdr-service.git
cd cdr-service
Соберите проект:

Для Maven:

bash
Copy
mvn clean install
Для Gradle:

bash
Copy
./gradlew build
Запустите приложение:

Для Maven:

bash
Copy
mvn spring-boot:run
Для Gradle:

bash
Copy
./gradlew bootRun
Откройте в браузере:

Перейдите по адресу: http://localhost:8080.
