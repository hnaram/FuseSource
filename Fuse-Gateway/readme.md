Fabric Gateway
===============

Files
=====

1. WebService (Simple-Rest-Fuse) for JBoss Fuse 6.2.1


Steps
======

#SingleMachine

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



#RemoteMachine Setup

Below is the setup at my end.

---------------------------------------------------------------------------------------------
JBossFuse:karaf@root> container-list
[id]          [version]  [type]  [connected]  [profiles]              [provision status]
root*         1.0        karaf   yes          fabric                  success           
                                              fabric-ensemble-0000-1                    
                                              jboss-fuse-full                           
  gateway-lb  1.0        karaf   yes          default                 success           
                                              gateway-http                              
ssh-a         1.0        karaf   yes          default                 success           
                                              deployprofile                             
                                              jboss-fuse-full                           
ssh-b         1.0        karaf   yes          default                 success           
                                              deployprofile                             
                                              jboss-fuse-full                           

---------------------------------------------------------------------------------------------

root/gateway-lb: running on host A

ssh-a: running on host B

ssh-b: running on host C


Below is the log snippet from "gateway-lb"

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
2016-05-25 10:20:37,800 | INFO  | entloop-thread-0 | HttpGatewayHandler               | 99 - io.fabric8.gateway-core - 1.2.0.redhat-621084 | Proxying request /cxf/users/UserService/get_data to service path: /cxf/users/UserService/get_data on service: http://B:8181/cxf/users reverseServiceUrl: http://0.0.0.0:9000/cxf/users

2016-05-25 10:26:17,394 | INFO  | entloop-thread-0 | HttpGatewayHandler               | 99 - io.fabric8.gateway-core - 1.2.0.redhat-621084 | Proxying request /cxf/users/UserService/get_data to service path: /cxf/users/UserService/get_data on service: http://C:8181/cxf/users reverseServiceUrl: http://0.0.0.0:9000/cxf/users
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


Below is the output of wget from A

---------------------------------------------------------------------------------------------
 wget -S -q -O - http://A:9000/cxf/users/UserService/get_data
  HTTP/1.1 200 OK
  Content-Type: application/json
  Date: Wed, 25 May 2016 00:31:08 GMT
  Content-Length: 35
  Server: Jetty(8.1.17.v20150415)
  Transfer-Encoding: chunked
This is standard response from REST
---------------------------------------------------------------------------------------------

Below is the output of curl from A

---------------------------------------------------------------------------------------------
 curl -i http://A:9000/cxf/users/UserService/get_data
HTTP/1.1 200 OK
Content-Type: application/json
Date: Wed, 25 May 2016 00:29:40 GMT
Content-Length: 35
Server: Jetty(8.1.17.v20150415)
Transfer-Encoding: chunked

This is standard response from REST
---------------------------------------------------------------------------------------------

and from A

---------------------------------------------------------------------------------------------
[hnaram@hnaram bin]$ curl -i http://A:8181/cxf/users/UserService/get_data
HTTP/1.1 200 OK
Content-Type: application/json
Date: Wed, 25 May 2016 00:29:01 GMT
Content-Length: 35
Server: Jetty(8.1.17.v20150415)

This is standard response from REST
---------------------------------------------------------------------------------------------

and from A

---------------------------------------------------------------------------------------------
[hnaram@hnaram bin]$ curl -i http://B:8181/cxf/users/UserService/get_data
HTTP/1.1 200 OK
Content-Type: application/json
Date: Wed, 25 May 2016 00:29:01 GMT
Content-Length: 35
Server: Jetty(8.1.17.v20150415)

This is standard response from REST
---------------------------------------------------------------------------------------------
