# Create AWS Entity - Create DynamoDB Table

[Click here to go back to the main page.](../../README.md)

  - Go to https://aws.amazon.com and login as `Root User`
    - The browser should go to https://console.aws.amazon.com/console/home
  - In the search box, enter 'DynamoDB' and select the 'DynamoDB' result
  - Click the 'Create table' button
  - Click the 'Tables' link in the left column
  - Click the 'Create table' button
  - In the 'Table name' field, enter `userAppData.#TF_VAR_AWS_DOMAIN_NAME#`
  - In the 'Partition key' field, enter `app_id`
  - Check the 'Add sort key' checkbox and enter `user_id` in field just below it
  - Scroll down the page and click the 'Create' button
  - Record the table name as the `TF_VAR_AWS_DYNAMODB_TABLE_NAME_USERAPPDATA` variable value

[Click here to go back to the main page.](../../README.md)
