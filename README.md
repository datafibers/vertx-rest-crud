# vertx-rest-crud

### Environment:
- VM with Linux (RedHat ?)
- Java 1.8.0_66
- MariaDB 5.5.44

### Requirements:
- Server application with REST API using Play Framework or Vert.x (decide by yourself and argue)    
- Simple authentication and authorization
- Connection to DB
- Support CRUD-operations
- Usage of KISS principle
- Build with Maven
- Documentation of public methods

### How to run:
1. Build the project with Maven:
```
mvn clean package
```

2. Launch the _fat jar_ as follows:
```
java -jar target/vertx-rest-crud-1.0-SNAPSHOT-fat.jar
```

3. Point your browser at [Welcome Page] (http://localhost:8080) to see a simple welcome page
4. Invoke REST API by this URL: [/rest/whiskys] (http://localhost:8080/rest/whiskys)
5. Use browser plugins for REST method invocations (e.g. [POSTMAN] (https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop))

### Running integration tests
* Requires deployment of a Verticle that runs HttpServer. This is achieved by running the fat-jar by maven plugins.
* Use the following to run the integration tests:
```
mvn clean verify
```
    
### REST API:
* POST /rest/whiskys => Create a new whisky
* GET /rest/whiskys => Get (read) all whiskys
* GET /rest/whiskys/id => Get (read) the whisky with the corresponding id
* PUT /rest/whiskys/id => Update whisky with the corresponding id
* DELETE /rest/whiskys/id => Delete whisky with the corresponding id
* Sample JSON:
```
{"name":"Bourbon", "origin":"USA"}
```

### Authentication and Authorization
* Simple Auth used based on io.vertx.ext.auth.shiro.ShiroAuth
* Users, passwords and roles declared (unencrypted) in file: **vertx-users.properties**
* Using REST API for CRUD (URL: /rest/whiskys*) requires authentication (username: tim, password: sausages)
* Only user with role:admin is authorized to use the REST API (URL: /rest/whiskys*) 
