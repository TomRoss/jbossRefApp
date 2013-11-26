To better work with support we created a demo application that contains our requirements.
It contains:
- 2 applications: minibankref1 and minibankref2
- cluster1: node_1A, node_1B 
- cluster2: node_2A, node_2B

Applications doesn't use any custom libraries.
We are based on windows.
and we currently use EAP-6.2.0-beta1

To run a node you will need to adapt the following script: miniBankRef\servers\common.cmd
and change the JAVA_HOME and the EAP_HOME
Then you will be able to run miniBankRef\servers\node_1A\start_node.cmd for instance


To complie the application you will need to use maven 3.
poms are selfcontaining and don't depend on specific artefacts.

