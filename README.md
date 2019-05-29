# YourTaxi 
Backend Applicant Test Service

## Task Description
You should be able to start the example application by executing com.yourtaxi.YourTaxiServerApplicantTestApplication, which starts a webserver on port 8080 (http://localhost:8080) and serves SwaggerUI where can inspect and try existing endpoints.

The project is based on a small web service which uses the following technologies:

* Java 1.8
* Spring MVC with Spring Boot
* Database H2 (In-Memory)
* Maven
* Swagger
* Intellij as IDE is preferred but not mandatory. We do provide code formatter for intellij and eclipse in the etc folder.
* Spring Cloud with Hystrix and Eureka
* Test and code quality
    * Junit
    * Sonarqube
    * Jacoco
    * MockMvc
    * Mockito
    * AssertJ
* TravisCI
* Docker
* Cache
* Spring Security

## How To RUN
The application dependencies are controlled by [Maven](https://maven.apache.org/,"Maven") the using the following command the application will be starter
	
	mvn spring-boot:run

## Sonarqube
To run sonarqube use the following command with sonarqube server running, for further information check [SonarQube-Website](https://www.sonarqube.org/,"Sonar")

    mvn clean install sonar:sonar
    
## Swagger UI - Local

	http://localhost:8090/swagger-ui.html

## Eureka Client

	http://localhost:8090/
	
## Spring Security
This project uses Basic Auth as authentication the username and password are:

* username: admin
* password: yourTaxitatus

	
## Database
The data is a Create type, so every time the Application run it resets the persisted Data. It is a in memory database. 
For more information see [H2-Website](http://www.h2database.com/html/main.html, "H2")

## Rest API
The application has two main controller one for Drivers and other for Cars.

### Filter
The Drivers Controller has a filter for some characteristic like:

* coordinate:lat^long -> coordinate:10^10
* status:ONLINE or status:OFFLINE
* username:username
* car.id:1
* car.engineType:GAS
* car.manufacturer:BMW
* car.rating:5, car.rating>3 or car.rating<4
* car.seatCount:5, car.seatCount>3 or car.seatCount<4

To access this url just hit the endpoint below (this is and example)

    http://localhost:8090/filter?search=car.id:1,car.rating>2.5
    

## Entities information
### Car Engine Type
For the engine there is 6 type:

* ELECTRIC,
* GAS,
* HYBRID_ELECTRIC,
* DIESEL,
* FLEX_FUEL,
* NATURAL_GAS    

### Car Seat Count
The value must be greater than 2 and lower than 9

### Car rating
The value must be greater or equals 0 and lower or equals 5


## Project Request

You should be aware of the following conventions while you are working on this exercise:

 * All new entities should have an ID with type of Long and a date_created with type of ZonedDateTime.
 * The architecture of the web service is built with the following components:
 	* DataTransferObjects: Objects which are used for outside communication via the API
   * Controller: Implements the processing logic of the web service, parsing of parameters and validation of in- and outputs.
   * Service: Implements the business logic and handles the access to the DataAccessObjects.
   * DataAccessObjects: Interface for the database. Inserts, updates, deletes and reads objects from the database.
   * DomainObjects: Functional Objects which might be persisted in the database.
 * TestDrivenDevelopment is a good choice, but it's up to you how you are testing your code.

You should commit into your local git repository and include the commit history into the final result.

### Task 1
 * Write a new Controller for maintaining cars (CRUD).
   * Decide on your own how the methods should look like.
   * Entity Car: Should have at least the following characteristics: license_plate, seat_count, convertible, rating, engine_type (electric, gas, ...)
   * Entity Manufacturer: Decide on your own if you will use a new table or just a string column in the car table.
 * Extend the DriverController to enable drivers to select a car they are driving with.
 * Extend the DriverController to enable drivers to deselect a car.
 * Extend the DriverDo to map the selected car to the driver.
 * Add example data to resources/data.sql
 
### Task 2
First come first serve: A car can be selected by exactly one ONLINE Driver. If a second driver tries to select a already used car you should throw a CarAlreadyInUseException.

### Task 3
Make use of the filter pattern to implement an endpoint in the DriverController to get a list of drivers with specific characteristics. Reuse the characteristics you implemented in task 1.

### Task 4
Security: secure the API. It's up to you how you are going to implement the security.