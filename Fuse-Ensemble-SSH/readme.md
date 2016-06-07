Environment Details


Root: 
Name: root
IP: 10.64.32.160

Child-1
Name: ssh-a
IP: 192.168.122.58

Child-2
Name: ssh-b
IP: 192.168.122.136


Steps

1. First start standalone fuse.

2. Then create a fabric on it 

fabric:create --wait-for-provisioning --verbose --clean --new-user admin --new-user-role admin --new-user-password admin --zookeeper-password passwd --resolver manualip --manual-ip 10.64.32.160

3. Make sure that the iptables and firewalld are stopped

4. Create a ssh container with below commands


fabric:container-create-ssh --host 192.168.122.58 --user root --password Wed@321 --new-user admin --new-user-password admin --resolver manualip --manual-ip 192.168.122.58 ssh-a


fabric:container-create-ssh --host 192.168.122.136 --user root --password Wed@321 --new-user admin --new-user-password admin --resolver manualip --manual-ip 192.168.122.136 ssh-b

5. If there is an issue check adding below entry in etc/System.properties of all the three containers

global.resolver=10.64.32.160

Also make sure that the service iptables and firewalld are stopped and sshd service is running.



6. Add these containers in an Ensemble

JBossFuse:karaf@root> ensemble-add ssh-a ssh-b

7. Check if the ssh containers have the ensemble profile on it 

JBossFuse:karaf@root> container-list 
[id]   [version]  [type]  [connected]  [profiles]              [provision status]
root*  1.0        karaf   yes          fabric                  success           
                                       fabric-ensemble-0000-1                    
                                       jboss-fuse-full                           
                                       fabric-ensemble-0001-1                    
ssh-a  1.0        karaf   yes          default                 success           
                                       fabric-ensemble-0001-2                    
ssh-b  1.0        karaf   yes          default                 success           
                                       fabric-ensemble-0001-3                    


And check the ensemble as below

JBossFuse:karaf@root> ensemble-list 
[id] 
root 
ssh-a
ssh-b











