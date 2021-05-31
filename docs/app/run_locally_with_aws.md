# Run This Application Locally _With_ AWS Integration

[Click here to go back to the main page.](../../README.md)

At this point, you should be able to run the application location _with_ AWS integration.

For this section, run all commands from the same command prompt.

## Stop and Confirm Completeness of Variable Values List

Confirm that a variables in the 'List of Variable Values' section above now have values.
You should have a script (like `~/Desktop/initVars.sh` if you're using a Mac) which
can set these environment variables on the command line.

## Remove Local Settings and Set Variable Values

'LOCAL' environment variables were used when the application was run locally _without_
AWS integration. When these variables are set, the application won't integrate with AWS.
Since you will confirm AWS integration in this section, these variables must now be unset.

From the command line, run the following commands to unset these 'LOCAL' variables.
(These instructions should work for both Windows and Mac.)

```
unset LOCAL_CMS_PATH
unset LOCAL_IAM_USER
unset LOCAL_USER_DATA
```

You can confirm that these values are no longer set by listing the current environment
variables with the `set` command on Windows and the `export` command on Mac.

## Set AWS Environment Variables

From the command line, run the script which initializes the variable values your recorded
as environment values. On Mac, the `source ~/Desktop/initVars.sh` command will do this.

You can confirm that these values are set by listing the current environment
variables with the `set` command on Windows and the `export` command on Mac.

## Upload Web Files to S3

Now that the application will be integrated with AWS, the web files used
by the web application will need to be uploaded to S3.

From the same directory as this file, run the following command from the command line.

```
aws s3 sync --acl public-read --exclude ".*" ./examples/coin_flip_game/content s3://#TF_VAR_AWS_S3_BUCKET_NAME_CONTENT#/content
```

### (Optional) Review S3 Bucket Contents

If the previous command completed without error, then the files successfully uploaded to S3.

But you have the option of logging into AWS as the `DevOps User` (obviously the `Root User`
could do this too because that user can do anything) and review the contents of the S3 bucket.

Here are the steps.
  - Open an incognito browser and go to `#TF_VAR_AWS_DEVOPS_CONSOLE_URL#`
  - Login as the `DevOps User`
  - In the search box, enter 'S3' and select the 'S3' result
  - Scroll down the table and select the `#TF_VAR_AWS_S3_BUCKET_NAME_CONTENT#` link
  under the 'Name'
  - Click the 'content/' link under the 'Name' column
  - Confirm that the (3) files in the './examples/coin_flip_game/content' are listed
  in the table

## Run Local Server and Use Application

Again, do the following.

1) Using Maven, assemble the project with command `mvn clean install`
2) Run the project with command `java -jar ./target/demo-0.0.1-SNAPSHOT.jar`
3) Open a browser and go to `http://localhost:8080/`
4) Login (this will require you to create an account) and play for a few minutes

Once finished, in the command prompt hold down the `Control` button then press `c`
to stop the local server.

### (Optional, But Recommended) Review Cognito User Group and DynamoDB Table

The user (you) that just played the game has saved information into AWS.

Here's how to examine that information.
  - Open an incognito browser and go to `#TF_VAR_AWS_DEVOPS_CONSOLE_URL#`
  - Login as the `DevOps User`
  - In the search box, enter 'Cognito' and select the 'Cognito' result
  - Click the 'Manage User Pools' button
    - Click the box labelled `#TF_VAR_AWS_DOMAIN_NAME#`
    - Click the 'Users and groups' link in the left column
    - Confirm that the username of the user who played the 'coin flip' game
    is listed under the 'Username' column
  - In the search box, enter 'DynamoDB' and select the 'DynamoDB' result
    - Click the 'Tables' link in the left column
    - Under the 'Name' column, click the `#TF_VAR_AWS_DYNAMODB_TABLE_NAME_USERAPPDATA#` link
    - Click the 'Items' tab
    - Confirm that the username of the user who played the 'coin flip' game
    is listed under the 'user_id' column and that user's high score appears
    in the JSON structure under the 'app_data' column
      - The value in the 'last_modified' column is the `epoch time` of the last update
      to this entry (sites like [this](https://www.epochconverter.com/) can be used to convert
      a number like this into human-readable time)

[Click here to go back to the main page.](../../README.md)
