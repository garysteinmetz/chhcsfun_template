# Maintenance

## Restart Hanging Server

Sometimes the server will stop responding to requests. Reboot the server to resolve
this problem.

Follow these instructions.

1) Login to the `AWS` console
2) Search for `Lightsail` and go to that selection
3) The server should be listed (under the `Instances` tab which is selected by default),
click the three vertical dots (this is strangely known as a 'hamberger' in the industry)
next to the server instance and click `Reboot` option then click the `Reboot` button
in the popup window
4) Wait a few minutes and then try to interact with an application running on the server

## Redeploy App

If you are interested in adding a feature to the deployed server or fixing a bug,
the application must be rebuilt and redeployed to the `Lightsail` instance.

Follow these instructions to do so.

### Rebuild the Application

1) Using Maven, assemble the project with command `mvn clean install`
2) Run the project with command `ls -la ./target/demo-0.0.1-SNAPSHOT.jar`
to confirm that the timestamp on the file is recent (it was just built in the previous step!)

### Upload the Application to S3

Upload the application to `S3` with the following command.

```
aws s3 cp ./target/demo-0.0.1-SNAPSHOT.jar s3://#S3_BUCKET_NAME#/
```

### Download the Application from S3 to the Lightsail Server

Follow these instructions.

1) Login to the `AWS` console
2) Search for `Lightsail` and go to that selection
3) The server should be listed (under the `Instances` tab which is selected by default),
click the three vertical dots (this is strangely known as a 'hamberger' in the industry)
next to the server instance and click `Connect` option
4) In the command prompt window that opens up, enter the following to download
the application from `S3` to the LightSail server

```
sudo aws s3 cp s3://#S3_BUCKET_NAME#/demo-0.0.1-SNAPSHOT.jar .
sudo chmod 777 ./demo-0.0.1-SNAPSHOT.jar
```

### Stop the Service (Web Server), Override Web Server File, Restart Service

Follow these commands to update the existing version of the web server with the new one.

#### Stop the Service (Web Server)

Issue the following command to stop the service.

```
sudo systemctl stop simple-app
```

#### Override Web Server File

Issue the following command to override the existing version of the web server
with the new one. Optionally rename or copy the existing `app.jar` file
before overwriting it (this existing file can be reverted to if there's a problem).

```
sudo cp ./demo-0.0.1-SNAPSHOT.jar ./app.jar
```

#### Start the Service (Web Server)

Issue the following command to start the service.

```
sudo systemctl start simple-app
```

Optionally enter `exit` in the remote terminal window to disconnect from the remote server.
(`AWS` times out the window pretty quickly though if nothing is done.)
