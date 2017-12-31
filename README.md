# featuretoggles
Microservice for managing feature toggles from multiple other services.

## Concepts

There are two concepts on this project:

* Toggle
* System

### Toggle
Often happens that a feature or a part of it must be turned on/off and this is where toggles come in. They can 
represent a button, a screen or even a whole feature (backend + frontend) and they should be switched off until they 
are ready for production. Once they are completely rolled out they don't serve a purpose anymore and should be 
deleted for the code's sake.

### System
In this project a system represents a (micro)service that typically is the only responsible for a set of features and
 therefore has its own toggles. When a cross-system feature is asked we need an 'universal' toggle that is not 
 associated to any system in particular.

## Technical Overview
This project maps a small micro service that allows toggles, systems and their associations to be created/managed.

External dependencies (no Dockerfile is provided but it shouldn't take long to have it up and running):
* Java (8 was used)
* Maven (3.0.5 was used)
* Postgres (9.6 was used)
* RabbitMQ (3.6.2 was used)

Other dependencies:
* Spring 4.3.x
* Swagger 2.x

**Postgres** is the toggle/system store where everything is persisted.
**RabbitMQ** serves the purpose of notifying systems about toggle changes

### Database schema
The **toggles** and **systems** tables are pretty straight forward, but the **system_toggles** is where the 
associations are made. Going into details about this table:

| column name   | Comments |
| ------------- |-------------|
| fk_system     | Foreign key. When _null_ means that this toggle is available for all systems |
| fk_toggle     | Foreign key. Mandatory      |
| enabled       | (bool) The toggle can be turned on/off for a specific system, so this is where the value should be stored      |
| allowed       | (bool) If true the toggle is white-listed for a system, otherwise is black-listed      |

![schema](https://github.com/goncalomalmeida/featuretoggles/blob/master/toggles.png "Database schema")

### Business logic
Apart from the trivial toggle/system creation the main logic of toggle retrieval is under the following method:

> com.experiments.toggles.services.SystemToggleService.list

In this method the following logic is implemented:
1. Fetch all toggles specific to the calling system (highest priority)
2. Fetch all 'universal' toggles and keep only the ones for which there is no specific one
3. Exclude the black-listed toggles

As for toggle/system associations the logic is pretty simple:
1. Check if there is an association already and replace their values
2. Broadcast the change by publishing a message to RabbitMQ using the _systemId_ in the routing key. The consumers 
can then create bindings that allow them to fetch messages they are interested in.

### How to build
Build the project with the following maven command:
> mvn clean install -DskipTests

### How to run
After building the project run it with the following command:
> java -jar target/toggles-0.0.1-SNAPSHOT.jar

The app will be running on port 8080 by default.

### How to run integration tests
> mvn clean compile test

### How secure is this app
Spring security has been configured with two users:

```
admin:admin
ADMIN
```

```
user:password
OPERATOR
```

* No request can be fulfilled if no _Basic_ authentication is provided.
* No request can be fulfilled if the user isn't an ADMIN

### How to use
Swagger has been integrated so that developers can easily read and test the api:

> http://localhost:8080/swagger-ui.html#/

Remember that all requests are protected, so use the _admin_ user credentials.


## Future improvements
* Dockerfile with all the external dependencies
* All tests are integration ones, not respecting the [Test Pyramid](https://martinfowler.com/bliki/TestPyramid.html).
 Unit tests could be added.
* Integration tests is using the same database as 'production'. Properties should be overwritten for testing purposes.
* More validations could be done on the inputs
* Custom error handling (relying on the spring's default)
* Load tests to check how much scalable is the solution
* Be a little bit more fault tolerant (i.e. rabbit or postgres down)