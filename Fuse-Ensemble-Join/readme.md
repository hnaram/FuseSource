There are 2 ways to create Ensemble, using create-ssh command and using the join command

This article talks about join command.

Consider the below example, there are 3 machines A, B and C


On A,
=====

Start JBoss Fuse

Create a Fabric

On B,
=====

Start JBoss Fuse

Execute the join command

--------------------------------------------------------------------------------------
JBossFuse:karaf@root> fabric:join --zookeeper-password admin localhost:2181 fabric-b
--------------------------------------------------------------------------------------

On C,
=====

Start JBoss Fuse

Execute the join command

--------------------------------------------------------------------------------------
JBossFuse:karaf@root> fabric:join --zookeeper-password admin localhost:2181 fabric-c
--------------------------------------------------------------------------------------

Create the Ensemble from A using the below command.

--------------------------------------------------------------------------------------
JBossFuse:karaf@root> ensemble-add fabric-b fabric-c 
This will change of the zookeeper connection string.
Are you sure want to proceed(yes/no):yes
Updated Zookeeper connection string: 10.64.33.68:2182,10.64.33.68:2183,10.64.33.68:2184
--------------------------------------------------------------------------------------

Check the Ensemble

--------------------------------------------------------------------------------------
JBossFuse:karaf@root> ensemble-list 
[id]    
root    
fabric-b
fabric-c
--------------------------------------------------------------------------------------

root, fabric-b and fabric-c are called as fabric servers. Now, we can create child containers on top of these fabric servers.

These container would be called as managed containers.

--------------------------------------------------------------------------------------   
JBossFuse:karaf@root> container-list 
[id]      [version]  [type]  [connected]  [profiles]              [provision status]
fabric-b  1.0        karaf   yes          fabric                  success           
                                          fabric-ensemble-0001-2                    
  b-1     1.0        karaf   yes          default                 success           
  b-2     1.0        karaf   yes          default                 success           
fabric-c  1.0        karaf   yes          fabric                  success           
                                          fabric-ensemble-0001-3                    
  c-1     1.0        karaf   yes          default                 success           
  c-2     1.0        karaf   yes          default                 success           
root*     1.0        karaf   yes          fabric                  success           
                                          jboss-fuse-full                           
                                          fabric-ensemble-0001-1                    
  r-1     1.0        karaf   yes          default                 success           
  r-2     1.0        karaf   yes          default                 success           
JBossFuse:karaf@root> 
--------------------------------------------------------------------------------------

