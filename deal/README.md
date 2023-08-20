# MVP Level 2 Реализация микросервиса Сделка (deal)

Реализация микросервиса Сделка (deal), основная задача которого - реализовывать основную бизнес-логику, взаимодействовать с БД и другими микросервисами.

## Структура БД: <br>
![image](https://github.com/AlekseyObukhov/conveyor/assets/133809437/90ec7837-15f5-4b45-b458-df6e2e7df8a8)

* **Зеленый:** сущность реализована в виде отдельного отношения <br>
* **Желтый:** сущность реализована в виде поля типа jsonb <br>
* **Синий:** сущность реализована в виде enum, сохранена в бд как varchar <br>

## Список API:
**1. `POST: /deal/application` - расчёт возможных условий кредита.** <br>
* **Request** - `LoanApplicationRequestDTO` <br>
* **Response** - `List<LoanOfferDTO>` <br>

По API приходит `LoanApplicationRequestDTO`. На основе `LoanApplicationRequestDTO` создаётся сущность `Client` и сохраняется в БД. Создаётся `Application` со связью на только что созданный `Client` и сохраняется в БД. Отправляется POST запрос на /conveyor/offers МС conveyor через `FeignClient`. Каждому элементу из списка `List<LoanOfferDTO>` присваивается id созданной заявки (Application) 
Ответ на API - список из 4х LoanOfferDTO от "худшего" к "лучшему" (чем меньше итоговая ставка, тем лучше). <br>

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

**2. `PUT: /deal/offer`** - Выбор одного из предложений. 

**Request** - `LoanOfferDTO` <br>
**Response** - `void` <br>

По API приходит `LoanOfferDTO.` Достаётся из БД заявка(Application) по applicationId из `LoanOfferDTO.` <br>
В заявке обновляется статус, история статусов(List<ApplicationStatusHistoryDTO>), принятое предложение LoanOfferDTO устанавливается в поле appliedOffer. Заявка сохраняется. <br>

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


**3. `PUT: calculate/{applicationId}`** - завершение регистрации + полный подсчёт кредита. <br>

**Request** - `FinishRegistrationRequestDTO,` param applicationId (Long) <br>
**Response** - `void` <br>

По API приходит объект `FinishRegistrationRequestDTO` и параметр applicationId (Long). Достаётся из БД заявка(Application) по applicationId. `ScoringDataDTO` насыщается информацией из `FinishRegistrationRequestDTO` и `Client,` который хранится в `Application.` Отправляется POST запрос к МС КК с телом `ScoringDataDTO.` 

**Example:** <br>
* **Request:** <br>
{ <br>
"gender": "MALE", <br>
"maritalStatus": "SINGLE", <br>
"dependentAmount": 0, <br>
"passportIssueDate": "2023-08-20", <br>
"passportIssueBranch": "string", <br>
"employment": { <br>
"employmentStatus": "EMPLOYED", <br>
"employerINN": "INN", <br>
"salary": 85000, <br>
"position": "WORKER", <br>
"workExperienceTotal": 18, <br>
"workExperienceCurrent": 6 <br>
}, <br>
"account": "3456897" <br>
} <br>



## Документация API
API задокументировано с помощью Swagger. <br>
http://localhost:8081/swagger-ui/index.html - ссылка для доступа к swagger-ui в браузере
