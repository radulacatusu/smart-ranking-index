# A smart ranking index application

This is a Spring Boot Application with a REST API
* Retrieves a time series containing the individual ranks for an ASIN, for a certain keyword
* Retrieves a time series containing the aggregated ranks for all ASINs for a certain keyword
* Retrieves a time series containing the aggregated ranks of all keywords for a certain ASIN
Extra:
* Retrieves a time series containing the aggregated ranks for each ASIN for a certain keyword
* Retrieves a time series containing the aggregated ranks for each keyword for a certain ASIN
* 
## About

The application is developed with Spring Boot 2.6.4, Java 17 and gradle as a building tool.

## To run the application
Use one of the several ways of running a Spring Boot application. Below are just three options:

1. Build using maven goal: `gradlew clean package` and execute the resulting artifact as follows `java -jar smart-ranking-index-0.0.1-SNAPSHOT.jar` or
2. On Unix/Linux based systems: run `./gradlew clean package` then run the resulting jar as any other executable `./smart-ranking-index-0.0.1-SNAPSHOT.jar`

## API Documentation

Swagger can be accessed at http://localhost:8080/swagger-ui/index.html

## Testing the API

1. Integration tests with @SpringBootTest
2. Mocking for the service layer
3. Unit tests

## To manually test the application

I used POSTMAN and cURL to test the application. There is a file with all the requests that can be imported in Postmen. 

## Built With

* [Gradle](https://gradle.org/) - Dependency Management

## Authors

* **Radu Stefan Lacatusu** - [radulacatusu](https://github.com/radulacatusu/)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
