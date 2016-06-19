# LiviuHomescu-boot-homework

Download:

git clone git@github.com:HomescuLiviu/LiviuHomescu-boot-homework.git

Install :

The application was developed in Intellij and deployed using maven.

run 'mvn package' under "/path/boot-homework-LiviuHomescu"

Deploy: 

The application was developed using Spring Boot.

mvn spring-boot:run

Logs can be found in "/path/boot-homework-LiviuHomescu/logfile.log".

Design:

The application has 3 layers:

1. The filters are uswed to redirect invalid requests that are between 0 a.m and 6 a.m. or
      that exeed the maximum number of accesses allowed per day. 
      
2. The LoanController which responds to requests and orders : 
      a. the loan to be stored in the database.
      b. the loan attempt to be stored in a cache. 

3. The LoanServie which :
      a. stores loans in the database.
      b. stores loan attempts to be stored in a cache. 

