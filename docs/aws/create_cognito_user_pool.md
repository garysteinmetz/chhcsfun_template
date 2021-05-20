# Create AWS Entity - Create Cognito User Pool

[Click here to go back to the main page.](../../README.md)


  - Go to https://aws.amazon.com and login as you would on normal Amazon
    - The browser should go to https://console.aws.amazon.com/console/home
  - Make sure you have selected the correct AWS region (like 'US East (N. Virginia) us-east-1')
  - In the search box, enter 'Cognito' and select the 'Cognito' result
  - Click the 'Manage User Pools' button
  - Click the 'Create a user pool' button
  - Enter `#TF_VAR_AWS_DOMAIN_NAME#` for the name of the user pool
  - Click the 'Review defaults' button
  - Scroll to the bottom of the page and click the 'Create pool' button
  - In the resulting page, record the 'Pool Id' value at the top
  as the `TF_VAR_AWS_COGNITO_USER_POOL_ID` variable value
  - Click the 'App clients' link in the left column
  - Click the 'Add an app client' button
  - Enter `#TF_VAR_AWS_DOMAIN_NAME#` in the 'App client name' field
  - Scroll down to the bottom of the page and click the 'Create app client' button
  - In the resulting page, click the 'Show Details' button
  - Under the 'App client id' section, record the value
  as the `TF_VAR_AWS_COGNITO_CLIENT_ID` variable value
  - Under the 'App client secret' section, record the value
  as the `TF_VAR_AWS_COGNITO_CLIENT_SECRET` variable value
  - Click the 'App client settings' link on the left side
  - In the 'Enabled Identity Providers' section, check the 'Select all' checkbox
  - In the 'Callback URL(s)' text box, enter the following
    - `http://localhost:8080/oauthTwoCallback, https://#TF_VAR_AWS_DOMAIN_NAME#/oauthTwoCallback`
  - Under the 'Allowed OAuth Flows' section, check the 'Authorization code grant' checkbox
  - Check all checkboxes under the 'Allowed OAuth Scopes' section
  - Click the 'Save changes' button
  - Click the 'Domain name' link on the left side
  - In the 'Domain prefix' text box, enter `#TF_VAR_AWS_DOMAIN_NAME#` but replace any '.' (period)
  with a '-' (hyphen)
  - Click the 'Check availability' button to confirm that that login domain name is available
  - Click the 'Save changes' button
  - Record the full URL (including 'https://' prefix and '.com' suffix)
  in the 'Amazon Cognito domain' section as the `TF_VAR_AWS_COGNITO_DOMAIN_NAME` variable value

Using the `TF_VAR_AWS_COGNITO_DOMAIN_NAME` variable value, derive these variable values.

  - `TF_VAR_AWS_COGNITO_OAUTH2_AUTHORIZE_URL`
    - `#TF_VAR_AWS_COGNITO_DOMAIN_NAME#/oauth2/authorize?response_type=code&client_id=#TF_VAR_AWS_COGNITO_CLIENT_ID#`
  - `TF_VAR_AWS_COGNITO_OAUTH2_TOKEN_URL`
    - `#TF_VAR_AWS_COGNITO_DOMAIN_NAME#/oauth2/token`

[Click here to go back to the main page.](../../README.md)
