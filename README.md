# SocialN Application

List of API exposed 


``` 

 POST /wall/{userName} - post a new message 
    ex. curl -d "My first message" http://localhost:8080/wall/paolo
    
    
 GET /wall/{userName} - return the user posted messages
     ex. curl http://localhost:8080/wall/paolo


 GET /wall/{userName}/following - return the user followed people
    ex. curl http://localhost:8080/wall/paolo/following
    
 
 PUT /wall/{userName}/follow/{userNameToFollow} - follow another user and return the list of the followed people
    ex. curl -XPUT http://localhost:8080/wall/paolo/follow/marco
    
 
 GET /wall/{userName}/timeline - return the list of the messages posted by all the followed people
    ex. curl http://localhost:8080/wall/paolo/timeline
    
   
```


##### Start application on default port

`mvn spring-boot:run`


##### Start application on a specific port

`mvn spring-boot:run -Dserver.port=8090`