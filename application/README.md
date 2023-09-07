# MVP Level 3 Перенос прескоринга в МС-application

Реализация микросервиса Заявка (application)<br>

## Список API:
**1. `POST: /application` - расчёт возможных условий кредита.** <br>
* **Request** - `LoanApplicationRequestDTO` <br>
* **Response** - `List<LoanOfferDTO>` <br>

По API приходит `LoanApplicationRequestDTO`. На основе LoanApplicationRequestDTO происходит прескоринг. Отправляется POST запрос на /deal/application МС deal через `FeignClient`.
Ответ на API - список из 4х LoanOfferDTO от "худшего" к "лучшему" (чем меньше итоговая ставка, тем лучше). <br>

Правила прескоринга:<br>

Имя, Фамилия - от 2 до 30 латинских букв. Отчество, при наличии - от 2 до 30 латинских букв. <br>
Сумма кредита - действительно число, большее или равное 10000. <br>
Срок кредита - целое число, большее или равное 6. <br>
Дата рождения - число в формате гггг-мм-дд, не позднее 18 лет с текущего дня. <br>
Email адрес - строка, подходящая под паттерн [\w\.]{2,50}@[\w\.]{2,20} <br>
Серия паспорта - 4 цифры, номер паспорта - 6 цифр. <br>

**Example:** <br>
* **Request:** <br>
{ <br>
  "amount": 100000, <br>
  "term": 12, <br>
  "firstName": "Ivan", <br>
  "lastName": "Ivanov", <br>
  "middleName": "Petrovich", <br>
  "email": "ivanov@gmail.com", <br>
  "birthdate": "1997-05-17", <br>
  "passportSeries": "1234", <br>
  "passportNumber": "123456" <br>
} <br>

* **Response:** <br>
![image](https://github.com/AlekseyObukhov/conveyor/assets/133809437/51ab9d55-7e9c-48ab-87cf-46f24bcaa730)


Клиент и заявка сохраняются в БД:
![image](https://github.com/AlekseyObukhov/conveyor/assets/133809437/0622153f-2381-42fd-8dd0-8fe59a90ffe3)
![image](https://github.com/AlekseyObukhov/conveyor/assets/133809437/9b22a3ad-d341-427a-9222-98e87b450a17)

**2. `PUT: /application/offer`** - Выбор одного из предложений. 

**Request** - `LoanOfferDTO` <br>
**Response** - `void` <br>

По API приходит `LoanOfferDTO.` Отправляется POST-запрос на /deal/offer в МС deal через FeignClient. <br>

**Example:** <br>
* **Request:** <br>
{ <br>
"applicationId": 1, <br>
"requestedAmount": 100000, <br>
"totalAmount": 103000, <br>
"term": 12, <br>
"monthlyPayment": 8817.6, <br>
"rate": 5, <br>
"isInsuranceEnabled": true, <br>
"isSalaryClient": true <br>
} <br>

Оффер добавляется в таблицу: <br>
![image](https://github.com/AlekseyObukhov/conveyor/assets/133809437/2bb2228f-6666-488e-8fd3-d3a3fa3e4cec)
Обновляется статус: <br>
![image](https://github.com/AlekseyObukhov/conveyor/assets/133809437/fa19a680-3a43-45bf-acd6-474bad1e58bc)

## Документация API
API задокументировано с помощью Swagger. <br>
http://localhost:8082/swagger-ui/index.html - ссылка для доступа к swagger-ui в браузере
