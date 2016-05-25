
############## CXF loadbalancing ####################

Steps
=====

There are two parts

----------------------------------------------------------------------------------
drwxr-xr-x. 3 hnaram hnaram 4096 May 25 18:31 Client
drwxr-xr-x. 3 hnaram hnaram 4096 May 25 18:32 Server
----------------------------------------------------------------------------------

Files inside are:

----------------------------------------------------------------------------------
├── Client
│   ├── pom.xml
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── redhat
│           │           └── Endpoint.java
│           └── resources
│               └── OSGI-INF
│                   └── blueprint
│                       └── blueprint.xml
└── Server
    ├── pom.xml
    └── src
        └── main
            ├── java
            │   └── com
            │       └── redhat
            │           └── Endpoint.java
            └── resources
                └── OSGI-INF
                    └── blueprint
                        └── blueprint.xml

18 directories, 6 files
----------------------------------------------------------------------------------

1. Start JBoss Fuse 6.2.1

2. Create a Fabric.

3. Create 3 child containers

----------------------------------------------------------------------------------
container-create-child root cxf-1
container-create-child root cxf-2
container-create-child root cxf-lb
----------------------------------------------------------------------------------

4. Create a profile and deploy the Server side Webservice on it

----------------------------------------------------------------------------------
profile-create deployprofile 
profile-edit -b mvn:com.redhat/cxf-rs-camel/6.2.1.redhat-084 deployprofile
----------------------------------------------------------------------------------

5. Create a profile and deploy the client on it

----------------------------------------------------------------------------------
profile-create deployprofile2 
profile-edit -b mvn:com.redhat/cxf-rs-camel-client/6.2.1.redhat-084 deployprofile2
----------------------------------------------------------------------------------

6. Add the Webservice on cxf-1 and cxf-2

----------------------------------------------------------------------------------
container-add-profile cxf-2 deployprofile
container-add-profile cxf-1 deployprofile
----------------------------------------------------------------------------------

7. Add the Client on cxf-lb

----------------------------------------------------------------------------------
container-add-profile cxf-lb deployprofile2
----------------------------------------------------------------------------------

8. Check all the containers

----------------------------------------------------------------------------------
JBossFuse:karaf@root> container-list
[id]      [version]  [type]  [connected]  [profiles]              [provision status]
root*     1.0        karaf   yes          fabric                  success           
                                          fabric-ensemble-0000-1                    
                                          jboss-fuse-full                           
  cxf-1   1.0        karaf   yes          default                 success           
                                          jboss-fuse-full                           
                                          deployprofile                             
  cxf-2   1.0        karaf   yes          default                 success           
                                          jboss-fuse-full                           
                                          deployprofile                             
  cxf-lb  1.0        karaf   yes          default                 success           
                                          jboss-fuse-full                           
                                          deployprofile2                            

----------------------------------------------------------------------------------

9. Check the webservice on cxf-1: http://localhost:8182/cxf/testendpoint/test/1

   Check the webservice on cxf-2: http://localhost:8183/cxf/testendpoint/test/1

10. Check the Client: http://localhost:1234/proxy/test/1

    Hit the url 5-6 times

11. Log of Client

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
2016-05-25 18:21:46,922 | INFO  | qtp879618566-131 | cxfProxyRoute                    | 144 - org.apache.camel.camel-core - 2.15.1.redhat-621084 | got proxy request
2016-05-25 18:21:52,618 | INFO  | qtp879618566-132 | cxfProxyRoute                    | 144 - org.apache.camel.camel-core - 2.15.1.redhat-621084 | got proxy request
2016-05-25 18:22:27,615 | INFO  | qtp879618566-130 | cxfProxyRoute                    | 144 - org.apache.camel.camel-core - 2.15.1.redhat-621084 | got proxy request
2016-05-25 18:23:11,057 | INFO  | qtp879618566-131 | cxfProxyRoute                    | 144 - org.apache.camel.camel-core - 2.15.1.redhat-621084 | got proxy request
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Log of cxf-1 
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
2016-05-25 18:23:14,156 | INFO  | qtp585193008-57  | LoggingInInterceptor             | 170 - org.apache.cxf.cxf-core - 3.0.4.redhat-621084 | Inbound Messagen  | ----------------------------n  | ID: 1n  | Address: http://10.64.33.68:8183/cxf/testendpoint/test/1n  | Http-Method: GETn  | Content-Type: */*n  | Headers: {Accept=[text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8], accept-encoding=[gzip, deflate, sdch], Accept-Language=[en-US,en;q=0.8], breadcrumbId=[ID-hnaram-pnq-csb-34388-1464164327578-0-7], Cache-Control=[max-age=0], connection=[keep-alive], content-type=[*/*], Host=[10.64.33.68:8183], org.apache.cxf.message.Message.PATH_INFO=[/test/1], org.apache.cxf.request.method=[GET], org.apache.cxf.request.uri=[/proxy/test/1], Pragma=[no-cache], Upgrade-Insecure-Requests=[1], User-Agent=[Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36]}n  | --------------------------------------
2016-05-25 18:23:14,200 | INFO  | qtp585193008-57  | cxfrsServerTest                  | 144 - org.apache.camel.camel-core - 2.15.1.redhat-621084 | The id contains 1!!
2016-05-25 18:23:14,239 | INFO  | qtp585193008-57  | LoggingOutInterceptor            | 170 - org.apache.cxf.cxf-core - 3.0.4.redhat-621084 | Outbound Messagen  | ---------------------------n  | ID: 1n  | Response-Code: 200n  | Content-Type: application/jsonn  | Headers: {Upgrade-Insecure-Requests=[1], connection=[keep-alive], Host=[10.64.33.68:8183], breadcrumbId=[ID-hnaram-pnq-csb-34388-1464164327578-0-7], User-Agent=[Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36], Cache-Control=[max-age=0], Pragma=[no-cache], id=[1], Accept-Language=[en-US,en;q=0.8], org.apache.cxf.request.method=[GET], org.apache.cxf.request.uri=[/proxy/test/1], accept-encoding=[gzip, deflate, sdch], Accept=[text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8], org.apache.cxf.message.Message.PATH_INFO=[/test/1], Content-Type=[application/json], Date=[Wed, 25 May 2016 08:23:14 GMT]}n  | Payload: this is a  responsen  | --------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Log of cxf-2
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
2016-05-25 18:22:30,630 | INFO  | qtp508404707-59  | LoggingInInterceptor             | 179 - org.apache.cxf.cxf-core - 3.0.4.redhat-621084 | Inbound Messagen  | ----------------------------n  | ID: 3n  | Address: http://10.64.33.68:8184/cxf/testendpoint/test/1n  | Http-Method: GETn  | Content-Type: */*n  | Headers: {Accept=[text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8], accept-encoding=[gzip, deflate, sdch], Accept-Language=[en-US,en;q=0.8], breadcrumbId=[ID-hnaram-pnq-csb-34388-1464164327578-0-5], Cache-Control=[max-age=0], connection=[keep-alive], content-type=[*/*], Host=[10.64.33.68:8184], org.apache.cxf.message.Message.PATH_INFO=[/test/1], org.apache.cxf.request.method=[GET], org.apache.cxf.request.uri=[/proxy/test/1], Pragma=[no-cache], Upgrade-Insecure-Requests=[1], User-Agent=[Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36]}n  | --------------------------------------
2016-05-25 18:22:30,633 | INFO  | qtp508404707-59  | cxfrsServerTest                  | 153 - org.apache.camel.camel-core - 2.15.1.redhat-621084 | The id contains 1!!
2016-05-25 18:22:30,636 | INFO  | qtp508404707-59  | LoggingOutInterceptor            | 179 - org.apache.cxf.cxf-core - 3.0.4.redhat-621084 | Outbound Messagen  | ---------------------------n  | ID: 3n  | Response-Code: 200n  | Content-Type: application/jsonn  | Headers: {Upgrade-Insecure-Requests=[1], connection=[keep-alive], Host=[10.64.33.68:8184], breadcrumbId=[ID-hnaram-pnq-csb-34388-1464164327578-0-5], User-Agent=[Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36], Cache-Control=[max-age=0], Pragma=[no-cache], id=[1], Accept-Language=[en-US,en;q=0.8], org.apache.cxf.request.method=[GET], org.apache.cxf.request.uri=[/proxy/test/1], accept-encoding=[gzip, deflate, sdch], Accept=[text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8], org.apache.cxf.message.Message.PATH_INFO=[/test/1], Content-Type=[application/json], Date=[Wed, 25 May 2016 08:22:30 GMT]}n  | Payload: this is a  responsen  | --------------------------------------

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
