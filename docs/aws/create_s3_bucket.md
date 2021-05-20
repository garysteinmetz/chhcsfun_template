# Create AWS Entity - Create S3 Bucket

[Click here to go back to the main page.](../../README.md)

First, create the bucket.

  - Go to https://aws.amazon.com and login as the `Root User`
    - The browser should go to https://console.aws.amazon.com/console/home
  - In the search box, enter 'S3' and select the 'S3' result
  - Click the 'Create bucket' button
  - Under 'Bucket name' enter `content.#TF_VAR_AWS_DOMAIN_NAME#`
  - Under the 'Block Public Access settings for this bucket' section,
  uncheck the 'Block all public access' checkbox and ensure all sub-checkboxes
  under it are unchecked too
  - Check the 'I acknowledge ...' checkbox just under that section
  - Scroll to the bottom of the page and click 'Create bucket' button
  - Record the name of this bucket as the `TF_VAR_AWS_S3_BUCKET_NAME_CONTENT` variable value

Now, allow the possibility of general access to this bucket. Note that this AWS account
will still need to grant separate access to each user.

  - You should now be back on the main S3 page
  ( https://s3.console.aws.amazon.com/s3/home )
  - Scroll down the table and click the `#TF_VAR_AWS_S3_BUCKET_NAME_CONTENT#` link under
  the 'Name' column
  - Click the 'Permissions' tab
  - Scroll down to the 'Bucket policy' section and click the 'Edit' button
  - In the text area under the 'Policy' section, enter the following then click
  the 'Save changes' button
    - Make sure to substitute `#TF_VAR_AWS_S3_BUCKET_NAME_CONTENT#`

```
{
    "Version": "2012-10-17",
    "Id": "Policy1617405356323",
    "Statement": [
        {
            "Sid": "Stmt1617405350836",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:*",
            "Resource": "arn:aws:s3:::#TF_VAR_AWS_S3_BUCKET_NAME_CONTENT#/*"
        },
        {
            "Sid": "Stmt1617405350836",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:*",
            "Resource": "arn:aws:s3:::#TF_VAR_AWS_S3_BUCKET_NAME_CONTENT#"
        }
    ]
}
```

[Click here to go back to the main page.](../../README.md)
