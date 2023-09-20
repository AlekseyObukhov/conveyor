# MVP Level 5 Реализация паттерна API-Gateway

Реализован микросервис, который будет выступать в роли API-Gateway для сервисов кредитного конвейера. <br>
Суть этого паттерна заключается в том, чтобы клиент отправлял запросы только в один микросервис (gateway), а уже он перенаправлял эти запросы в другие микросервисы с бизнес-логикой.

API MS-application:

**1. `POST: /gateway/application`** - расчёт возможных условий кредита. <br>

**2. `PUT: /gateway/application/offer`** - Выбор одного из предложений.

API MS-deal:

**1. `PUT: /gateway/calculate/{applicationId}`** - завершение регистрации + полный подсчёт кредита. <br>

**2. `POST: /gateway/document/{applicationId}/send`** - запрос на отправку документов. <br>

**3. `POST: /gateway/document/{applicationId}/sign`** - запрос на подписание документов. <br>

**4. `POST: /gateway/document/{applicationId}/code`** - подписание документов. <br>

**5. `GET: /admin/application/{applicationId}`** - получить заявку по id. <br>

**6. `GET: /admin/application`** - получение списка всех заявок. <br>


## Документация API<br>
API задокументировано с помощью Swagger. <br>
http://localhost:8090/swagger-ui/index.html - ссылка для доступа к swagger-ui в браузере