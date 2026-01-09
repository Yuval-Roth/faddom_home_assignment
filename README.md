# Build and run the project

#### Prerequisites 
* java 17+
* maven
* node.js
  
\-\-\-\-\-\-\-

### Running the backend server
navigate to the backend_server folder and run `mvn clean package` to build the jar. \
To run the backend server **you are required to supply the complete path to the AWS credentials file**

The credentials file **must** be in the following format:
```
aws_access_key_id = value
aws_secret_access_key = value
```

run the jar with the command: 

`java -jar target\backend_server-0.0.1-SNAPSHOT.jar --aws.credentials-path="C:\path\to\credentials\file"`

### Running the web server
navigate to the web_server folder and run `npm install` to install the required packages. \
run the web server with `npm start`

\-\-\-\-\-\-\-

Once both servers are running, navigate to http://localhost:4200/

Here is an example of how the page should look like once data is fetched:
![example](https://github.com/Yuval-Roth/faddom_home_assignment/blob/main/example.png?raw=true)


