Fabric Gateway
===============

Files
=====

1. WebService (Simple-Rest-Fuse) for JBoss Fuse 6.2.1


Steps
======

To deploy this service in a non-fabric env (not related to this usecase)

------------------------------------------------------------------------------------
osgi:install -s mvn:com.redhat/cxf-jaxrs-service/6.2.1.redhat-084
------------------------------------------------------------------------------------

Create 3 containers like below

------------------------------------------------------------------------------------
container-create-child root gateway-1
container-create-child root gateway-2
container-create-child root gateway-lb
------------------------------------------------------------------------------------

Create a profile

------------------------------------------------------------------------------------
profile-create deployprofile 
------------------------------------------------------------------------------------

Deploy a webservice on this profile

------------------------------------------------------------------------------------
profile-edit -b mvn:com.redhat/cxf-jaxrs-service/6.2.1.redhat-084 deployprofile
------------------------------------------------------------------------------------

Add this profile to the two child container 

------------------------------------------------------------------------------------
container-add-profile gateway-1 deployprofile
container-add-profile gateway-2 deployprofile
------------------------------------------------------------------------------------

Add gateway-http to the third container

------------------------------------------------------------------------------------
container-add-profile gateway-lb gateway-http 
------------------------------------------------------------------------------------

Access this service on browser: 

------------------------------------------------------------------------------------
gateway-1: http://localhost:8182/cxf/users/UserService/get_data

gateway-2: http://localhost:8183/cxf/users/UserService/get_data
------------------------------------------------------------------------------------

Access this service through LoadBalancer which runs on port 9000

------------------------------------------------------------------------------------
gateway-1: http://localhost:9000/cxf/users/UserService/get_data
------------------------------------------------------------------------------------

When we check the server.log we would see 

------------------------------------------------------------------------------------------------------------------------------------------------------------------------
2016-05-24 18:12:18,140 | INFO  | qtp434650709-194 | UserService                      | 279 - rest-service - 6.2.1.redhat-084 | Service is invoked !!!
------------------------------------------------------------------------------------------------------------------------------------------------------------------------

For this line to be printed in the "fuse.log" we need to uncomment below line "org.ops4j.pax.logging.cfg"

------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# use this for source code lines enabled and full thread and class names
#log4j.appender.out.layout.ConversionPattern=%d{ABSOLUTE} | %-5.5p | {%t} [%C] (%F:%L) | %X{bundle.id} - %X{bundle.name} - %X{bundle.version} | %m%n
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
