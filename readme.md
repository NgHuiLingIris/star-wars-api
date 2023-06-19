# Star Wars Information API
This application provides Star Wars information fetched from https://swapi.dev/api/ using a RESTful API.

# Requirements
JDK 1.8 or later
Maven 3.2+

# Building the application
You can build the application by running the following command from the root directory of the project:
`mvn clean install`
This command cleans the target directory, compiles your source code, runs your unit tests, and packages the compiled code into a JAR file within the target directory.

# Running the application
After building the application, you can run it using the Spring Boot Maven plugin:
`mvn spring-boot:run`
The application will start, and you can access it at http://localhost:8080.

# Running the tests
This application comes with some unit tests written using JUnit. You can run these tests using the following command:
`mvn test -e`
The -e flag is used for "errors" and it provides more details about errors that occur while running tests.
