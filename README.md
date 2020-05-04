# Customer Service

### Components/Libraries Used
Spring Boot  
Spring Data Jpa  
Swagger(Rest Api)  
H2(In memory Database)
Flyway(Db Migration)  
Jib maven plugin(dockerization)
Logback(logging)

### Resources
Docker Image : https://hub.docker.com/repository/docker/pvpkiran/customer-service
Github Repository:   

### Instructions
To run the application, execute the following command. Application is configured to run on port 8060.  
  
`
docker run -d -p 8060:8060 -e "SPRING_PROFILES_ACTIVE=dev" --name customer-service pvpkiran/customer-service
`
### Guides

