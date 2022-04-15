How to run
You need Java 16 and Docker and latest postgresql 

Cd into clode directiry, execute ./gradlew bootRun and you can access api at port 8080

API Description
Swagger UI is accessable via http://localhost:8080/swagger-ui.html

What is missing:

1. Security 
2. Ops
   1. Pipeline
   2. Comply with 12 factor: expose creds via env variable, logs to stdout
   3. Docker + k8s config
   4. Observability: integrate app with Promethius
3. Caching for get request
4. Testing:
   1. Integration for controllers
   2. Load testing
5. API improvement:
   1. Add pagiantion for GET methods

Architecture:
I would rewrite this app in the more scalable way. Introduce two services:
1. order-persister service - persist orders
2. disbursment-calculator - calculate disbursments
3. link them via queue and introduce change-data-capture to listen on changes at order table and send these
events to queue where these events would be processed by disbursment-calculator