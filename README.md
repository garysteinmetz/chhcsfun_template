# chhcsfun_template

Instructions for correctly creating a username-based user pool
Creating proper application-based user
  - Create a user in the IAM console, get 'Access keys', then assign it correct roles
  - Add appropriate permissions under permissions tab
    - AmazonS3FullAccess
    - AmazonDynamoDBFullAccess
    - IAMReadOnlyAccess
    - AmazonCognitoPowerUser
    - AWSResourceAccessManagerReadOnlyAccess
Creating DynamoDB tables
  - table name - UserAppData
  - partition_key - author_id
  - sort_key - user_app_id
  //
  - This is where 'app data' goes but it doesn't need to be formally declared - app_data
Redirect existing content

Generating Client ID-Secret pair


Cognito Settings (for user pool 'chhcsfun')
  - 'Username - Users can use a username and optionally multiple alternatives to sign up and sign in.'
    - 'Also allow sign in with verified email address'
  - Make sure 'email' and 'name' are both required attributes
  //
  - Set 'Domain name' of user pool to 'chhcsfun'
  //
  - Create 'App client' named 'Sample1'
  - Create 'App client settings' of 'Sample1'
    - For 'Enabled Identity Providers' check 'Select all'
    - For 'Callback URL(s)' enter - http://localhost:8080/login, https://chhcsfun.com/login, https://www.chhcsfun.com/login
    - Under 'Allowed OAuth Flows' check 'Authorization code grant'
    - Under 'Allowed OAuth Scopes' check everything
    - Click 'Save Changes'
    //
  - Create 'developers' group

./mvnw clean package spring-boot:repackage

Create SSL certificate for ELB in AWS Certificate Manager
  - Click on the 'Pending validation' option and select 'Create record in Route 53'
