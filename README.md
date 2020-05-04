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
Github Repository: https://github.com/pvpkiran/customerservice

### Instructions
To run the application, execute the following command. Application is configured to run on port 8060.  
  
`
docker run -d -p 8060:8060 -e "SPRING_PROFILES_ACTIVE=dev" --name customer-service pvpkiran/customer-service
`
### Guides
**Q1. How would you model this?**  
Various types of customers are handled with an enum CustomerType and with three different datetime fields 

createdOn ---> Indicates when the initial entry of customer is made into the system. CustomerType will be LEAD.  
contactedOn ---> Indicates when the lead is contacted. CustomerType will be PROSPECT.  
onboardedOn ---> Indicates when the prospect accepts the offer. CustomerType will Be ONBOARDED.  
 
**Q2. How would you model this in Spring boot?**  
This is done with Many to Many relation from customer to tag. Check Customer and Tag class.  

**Q3. How would you model this in spring boot?**  
This is done with a map attribute in Customer class. The attributes are stored as a separate collection.  

Once the application starts. Try reaching http://localhost:8060/swagger-ui.html

You can see that Api versioning is also taken into account while designing rest api's. 
Try calling different endpoints to see all the required results.  

Before calling any endpoint, make sure you call the _**/initialize**_(can be seen in swagger documentation) endpoint first.
 This will insert mock data into the inmemory database.  
  With Swagger in place, you do not need any external REST client to try it out.
  There is an option to try out api endpoints in Swagger.