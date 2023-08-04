MVP Level 1 Реализация кредитного конвейера

1. POST: /conveyor/offers - расчёт возможных условий кредита. 
Request - LoanApplicationRequestDTO 
Response - List<LoanOfferDTO>

По API приходит LoanApplicationRequestDTO.
На основании LoanApplicationRequestDTO происходит прескоринг, создаётся 4 кредитных предложения LoanOfferDTO на основании всех возможных комбинаций булевских полей isInsuranceEnabled и isSalaryClient (false-false, false-true, true-false, true-true). 
Стоимость страховки составляет 3% от суммы кредита, добавляется в тело кредита. При наличии страховки ставка уменьшается на 2%. Для зарплатных клиентов ставка уменьшается на 1%.
Ответ на API - список из 4х LoanOfferDTO от "худшего" к "лучшему" (чем меньше итоговая ставка, тем лучше).

Example:
Request:
{
  "amount": 400000,
  "term": 12,
  "firstName": "Ivan",
  "lastName": "Ivanov",
  "middleName": "Petrovich",
  "email": "ivanov@gmail.com",
  "birthdate": "1999-11-13",
  "passportSeries": "1234",
  "passportNumber": "123456"
}

Response:
{
    "applicationId": 1,
    "requestedAmount": 400000,
    "totalAmount": 400000,
    "term": 12,
    "monthlyPayment": 34795.37,
    "rate": 8,
    "isInsuranceEnabled": false,
    "isSalaryClient": false
  },
  {
    "applicationId": 3,
    "requestedAmount": 400000,
    "totalAmount": 400000,
    "term": 12,
    "monthlyPayment": 34610.54,
    "rate": 7,
    "isInsuranceEnabled": false,
    "isSalaryClient": true
  },
  {
    "applicationId": 2,
    "requestedAmount": 400000,
    "totalAmount": 412000,
    "term": 12,
    "monthlyPayment": 35459.27,
    "rate": 6,
    "isInsuranceEnabled": true,
    "isSalaryClient": false
  },
  {
    "applicationId": 4,
    "requestedAmount": 400000,
    "totalAmount": 412000,
    "term": 12,
    "monthlyPayment": 35270.41,
    "rate": 5,
    "isInsuranceEnabled": true,
    "isSalaryClient": true
  }

2. POST: /conveyor/calculation - валидация присланных данных + скоринг данных + полный расчет параметров кредита.
Происходит скоринг данных, высчитывание ставки(rate), полной стоимости кредита(psk), размер ежемесячного платежа(monthlyPayment), график ежемесячных платежей (List<PaymentScheduleElement>).

Request - ScoringDataDTO
Response - CreditDTO

Example:
Request:
{
  "amount": 400000,
  "term": 6,
  "firstName": "Ivan",
  "lastName": "Ivanov",
  "middleName": "Ivanovich",
  "gender": "MALE",
  "birthdate": "1999-11-13",
  "passportSeries": "1887",
  "passportNumber": "480095",
  "passportIssueDate": "2023-08-04",
  "passportIssueBranch": "FMS",
  "maritalStatus": "SINGLE",
  "dependentAmount": 1,
  "employment": {
    "employmentStatus": "EMPLOYED",
    "employerINN": "string",
    "salary": 60000,
    "position": "WORKER",
    "workExperienceTotal": 16,
    "workExperienceCurrent": 6
  },
  "account": "sag34tsgr",
  "isInsuranceEnabled": true,
  "isSalaryClient": true
}

Response:
{
  "amount": 412000,
  "term": 6,
  "monthlyPayment": 69671.36,
  "rate": 5,
  "psk": 3,
  "isInsurancyEnabled": true,
  "isSalaryClient": true,
  "paymentSchedule": 
    {
      "number": 1,
      "date": "2023-09-04",
      "totalPayment": 69671.36,
      "interestPayment": 1716.8,
      "debtPayment": 67954.56,
      "remainingDebt": 344045.44
    },
    {
      "number": 2,
      "date": "2023-10-04",
      "totalPayment": 69671.36,
      "interestPayment": 1433.64,
      "debtPayment": 68237.72,
      "remainingDebt": 275807.72
    },
    {
      "number": 3,
      "date": "2023-11-04",
      "totalPayment": 69671.36,
      "interestPayment": 1149.29,
      "debtPayment": 68522.07,
      "remainingDebt": 207285.65
    },
    {
      "number": 4,
      "date": "2023-12-04",
      "totalPayment": 69671.36,
      "interestPayment": 863.76,
      "debtPayment": 68807.6,
      "remainingDebt": 138478.05
    },
    {
      "number": 5,
      "date": "2024-01-04",
      "totalPayment": 69671.36,
      "interestPayment": 577.04,
      "debtPayment": 69094.32,
      "remainingDebt": 69383.73
    },
    {
      "number": 6,
      "date": "2024-02-04",
      "totalPayment": 69671.36,
      "interestPayment": 289.12,
      "debtPayment": 69382.24,
      "remainingDebt": 1.49
    }
}
