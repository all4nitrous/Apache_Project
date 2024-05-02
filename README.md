ID 31658908
Code for docker container: 22d3448a4606fce2cd201ccaee5f09ea117d218caa47f4af28b9b6ff519746d8
Github Link with docker container link: https://github.com/all4nitrous/Apache_Project 

I started by setting up the environment, selecting, and launching four Ubuntu Server EC2 instances from the AWS Management Console. I made sure each instance had a public IP and configured them to stop rather than terminate upon shutdown. Additionally, I established security groups to enable SSH and Spark communication between the nodes.  

  

For the software installation, I SSH'ed into each EC2 instance to install the Java Development Kit using the package manager and then proceeded to download and install Apache Spark, preparing my instances for cluster formation. 

 ![Spark cluster](https://github.com/all4nitrous/Apache_Project/assets/114439546/af27cae5-d4d7-4c00-b5f8-1e2fd4805f1d)


  

I moved on to cluster configuration, designating one instance as the Spark master node using the `start-master.sh` script and setting up the others as worker nodes with the `start-slave.sh` script, directing them to connect to the master node's IP. 

 ![master](https://github.com/all4nitrous/Apache_Project/assets/114439546/08a2ed5f-351f-4b54-af82-87e6b990ff90)

![rules](https://github.com/all4nitrous/Apache_Project/assets/114439546/e8954483-d688-4d26-bea0-2f351ed4d3cf)

  

I created a Maven project in IntelliJ IDEA, configured the `pom.xml` to include dependencies for Spark and MLlib, and then coded the application in Java. This application was designed to load and preprocess data, train a Logistic Regression model using Spark's MLlib, and validate the model's performance against a separate dataset. 

  

When it came to model training and validation, I employed the training dataset to fit the model and the validation dataset to assess its performance, specifically calculating the F1 Score as a measure of accuracy. 

  

For packaging and deployment, I used Maven to compile and package the project into a JAR file, which I then deployed to the Spark master node. I executed the application across the cluster using `spark-submit`, which ran the application in a distributed manner. 

  ![Docker-container](https://github.com/all4nitrous/Apache_Project/assets/114439546/93647681-94fc-4e73-a1d7-99fbb5ef00f0)


I then dockerized the Spark application, creating a Docker container that encapsulated all necessary dependencies.  

  

Lastly, I uploaded the Docker image to Docker Hub 
