# Meeting 
Meeting service used for booking requests for meetings in the boardroom.

# Architecture
* Following a hexagonal architecture.
* Core business logic resides in `application` module
* `application` module interacts with external dependencies using `adapters`

# Modules
* `application`
   1. Core business logic for the service resides here
   2. Uses `adapters` to interact with external components (web/persistence etc)
   3. `port` defines different adapters used by the core business logic
* `common` 
   1. Common utilies used across modules goes here
* `adapters`
   1. Concrete implementation of different adapters used by `application`
   2. Modules:
      * meeting-persistence
         * Dummy persistence adapter added for demonstration purpose only
      * meeting-console
         * Console application adapter, that implements input/ouput adapter for the business logic
* `configuration`
   1. Orchestrator for different components of meeting application
   2. Contains the main entry point for the application
   2. Currently configured to integrate `application` and all `adapters`
   


# How to run 

```
        mvn clean install
        java -jar configuration/target/configuration-0.0.1-SNAPSHOT.jar

```
* Application starts in console mode taking input from console
* New line after last input starts processing from console

# Scopes to improve
  1. Test Coverage: 
      * Currently tests are added only for business logic `application` module. This needs to be improved
      * Integration tests are not added currently
  2. Persistence of entity is a dummy module. Persistence of calendar needs to be defined
  3. Exception handling is more generic, and needs to be more refined.
  4. Generic validator used covers only null case currently. Need to improved to accommodate more generic validations
  
