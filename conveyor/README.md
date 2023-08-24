# MVP Level 1 Реализация кредитного конвейера

**1. `POST: /conveyor/offers` - расчёт возможных условий кредита.** <br>
* **Request** - `LoanApplicationRequestDTO` <br>
* **Response** - `List<LoanOfferDTO>` <br>

По API приходит `LoanApplicationRequestDTO`. На основании `LoanApplicationRequestDTO` происходит прескоринг, создаётся 4 кредитных предложения `LoanOfferDTO` на основании всех возможных комбинаций булевских полей isInsuranceEnabled и isSalaryClient (false-false, false-true, true-false, true-true). <br>
Стоимость страховки составляет 3% от суммы кредита, добавляется в тело кредита. При наличии страховки ставка уменьшается на 2%. Для зарплатных клиентов ставка уменьшается на 1%.<br>
Ответ на API - список из 4х `LoanOfferDTO` от "худшего" к "лучшему" (чем меньше итоговая ставка, тем лучше).

**Example:** <br>
* **Request:** <br>
  { <br>
  "amount": 400000, <br>
  "term": 12, <br>
  "firstName": "Ivan", <br>
  "lastName": "Ivanov", <br>
  "middleName": "Petrovich", <br>
  "email": "ivanov@gmail.com", <br>
  "birthdate": "1999-11-13", <br>
  "passportSeries": "1234", <br>
  "passportNumber": "123456" <br>
  } <br>

* **Response:** <br>
  { <br>
  "applicationId": 1, <br>
  "requestedAmount": 400000, <br>
  "totalAmount": 400000, <br>
  "term": 12, <br>
  "monthlyPayment": 34795.37, <br>
  "rate": 8, <br>
  "isInsuranceEnabled": false, <br>
  "isSalaryClient": false <br>
  }, <br>
  { <br>
  "applicationId": 3,<br>
  "requestedAmount": 400000,<br>
  "totalAmount": 400000,<br>
  "term": 12,<br>
  "monthlyPayment": 34610.54,<br>
  "rate": 7,<br>
  "isInsuranceEnabled": false,<br>
  "isSalaryClient": true<br>
  },<br>
  {<br>
  "applicationId": 2,<br>
  "requestedAmount": 400000,<br>
  "totalAmount": 412000,<br>
  "term": 12,<br>
  "monthlyPayment": 35459.27,<br>
  "rate": 6,<br>
  "isInsuranceEnabled": true,<br>
  "isSalaryClient": false<br>
  },<br>
  {<br>
  "applicationId": 4,<br>
  "requestedAmount": 400000,<br>
  "totalAmount": 412000,<br>
  "term": 12,<br>
  "monthlyPayment": 35270.41,<br>
  "rate": 5,<br>
  "isInsuranceEnabled": true,<br>
  "isSalaryClient": true<br>
  }<br>

**2. `POST: /conveyor/calculation`** - валидация присланных данных + скоринг данных + полный расчет параметров кредита. <br>
Происходит скоринг данных, высчитывание ставки(rate), полной стоимости кредита (psk), размер ежемесячного платежа (monthlyPayment), график ежемесячных платежей (List<PaymentScheduleElement>).

**Request** - `ScoringDataDTO` <br>
**Response** - `CreditDTO` <br>

**Example:** <br>
* **Request:** <br>
  { <br>
  "amount": 400000, <br>
  "term": 6, <br>
  "firstName": "Ivan",<br>
  "lastName": "Ivanov",<br>
  "middleName": "Ivanovich",<br>
  "gender": "MALE",<br>
  "birthdate": "1999-11-13",<br>
  "passportSeries": "1887",<br>
  "passportNumber": "480095",<br>
  "passportIssueDate": "2023-08-04",<br>
  "passportIssueBranch": "FMS",<br>
  "maritalStatus": "SINGLE",<br>
  "dependentAmount": 1,<br>
  "employment": {<br>
  "employmentStatus": "EMPLOYED",<br>
  "employerINN": "string",<br>
  "salary": 60000,<br>
  "position": "WORKER",<br>
  "workExperienceTotal": 16,<br>
  "workExperienceCurrent": 6<br>
  },<br>
  "account": "sag34tsgr",<br>
  "isInsuranceEnabled": true,<br>
  "isSalaryClient": true<br>
  }<br>

* **Response:**
  {<br>
  "amount": 412000,<br>
  "term": 6,<br>
  "monthlyPayment": 69671.36,<br>
  "rate": 5,<br>
  "psk": 3,<br>
  "isInsurancyEnabled": true,<br>
  "isSalaryClient": true,<br>
  "paymentSchedule": <br>
  {<br>
  "number": 1,<br>
  "date": "2023-09-04",<br>
  "totalPayment": 69671.36,<br>
  "interestPayment": 1716.8,<br>
  "debtPayment": 67954.56,<br>
  "remainingDebt": 344045.44<br>
  },<br>
  {<br>
  "number": 2,<br>
  "date": "2023-10-04",<br>
  "totalPayment": 69671.36,<br>
  "interestPayment": 1433.64,<br>
  "debtPayment": 68237.72,<br>
  "remainingDebt": 275807.72<br>
  },<br>
  {<br>
  "number": 3,<br>
  "date": "2023-11-04",<br>
  "totalPayment": 69671.36,<br>
  "interestPayment": 1149.29,<br>
  "debtPayment": 68522.07,<br>
  "remainingDebt": 207285.65<br>
  },<br>
  {<br>
  "number": 4,<br>
  "date": "2023-12-04",<br>
  "totalPayment": 69671.36,<br>
  "interestPayment": 863.76,<br>
  "debtPayment": 68807.6,<br>
  "remainingDebt": 138478.05<br>
  },<br>
  {<br>
  "number": 5,<br>
  "date": "2024-01-04",<br>
  "totalPayment": 69671.36,<br>
  "interestPayment": 577.04,<br>
  "debtPayment": 69094.32,<br>
  "remainingDebt": 69383.73<br>
  },<br>
  {<br>
  "number": 6,<br>
  "date": "2024-02-04",<br>
  "totalPayment": 69671.36,<br>
  "interestPayment": 289.12,<br>
  "debtPayment": 69382.24,<br>
  "remainingDebt": 1.49<br>
  }<br>
  }<br>
