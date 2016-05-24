Hi,

The project when deployed at my end gave the same error.

On Second thoughts, It would best if we deploy the webservice in bundle packaging and using blueprint xml file.

Refer [1] which also conveys the same

It may be possible to deploy a war but would take additional configuration. Let me know if it is fine to have a bundle deployment or is it a strict requirement to have a WAR deployment.

I have attached a TestCase (rest-cxf-example.zip) which would be deployed easily on JBoss Fuse 6.2.0.

Inside the zip you would see 

--------------------------------------------------------------------------------------------------------------
├── pom.xml
└── src
    └── main
        ├── java
        │   └── com
        │       └── redhat
        │           └── UserService.java
        └── resources
            └── OSGI-INF
                └── blueprint
                    └── blueprint.xml

--------------------------------------------------------------------------------------------------------------

UserService.java: is our rest service.

blueprint.xml: Has the required configuration for hosting the webservice

To deploy this project at your end.

1. Unzip "rest-cxf-example.zip"

2. Run: mvn clean install

3. Start JBoss Fuse 6.2.0

4. Run the command: 
  
   JBossFuse:karaf@root> osgi:install -s mvn:com.redhat/cxf-jaxrs-service/6.2.0.redhat-133

5. To see the webervices on the server open a browser and hit the url: http://localhost:8181/cxf

   By default all the webservices are hosted in the context "cxf"

6. To hit our rest webservice we would need to go to url: http://localhost:8181/cxf/users/UserService/get_data
   
7. Using Soap-UI we can check the same under the Json tab


[1] https://access.redhat.com/solutions/1383773

Regards,
Herambh Naram.
JBoss Middleware Team.
Red Hat
