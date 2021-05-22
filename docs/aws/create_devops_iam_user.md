# Create AWS Entity - Create `DevOps` IAM User

[Click here to go back to the main page.](../../README.md)

As mentioned, you should not use the same ('root') user who pays the bills as the user
who installs and maintains the application.

Follow these steps to create this other (specialized 'DevOps') user.

##### Create the Specialized DevOps User Which Will Be Used to Install and Maintain the App

  - Go to https://aws.amazon.com and login as you would on normal Amazon
  - In the search box, enter 'IAM' and select the 'IAM' result
  - In the left column, click the 'Users' link
  - In the 'User name' text box, enter a username of your choice (like `'devops_user'`)
  - Under 'Access type,' select both 'Progammatic access' (from `aws` tool)
  and 'AWS Management Console access' (from browser) options
    - Record this value as the `TF_VAR_AWS_DEVOPS_USERNAME` variable value
  - Under the 'Console password' section, select the 'Custom password' option
  then enter a password of your choosing
    - Record this value as the `TF_VAR_AWS_DEVOPS_PASSWORD` variable value
    - This is a secret value that shouldn't be shared with others
  - Uncheck the 'Require password reset' option
  - Click the 'Next: Permissions' button
  - For now, ignore adding permissions and instead click the 'Next: Tags' button
  - Click the 'Next: Review' button
  - Click the 'Create user' button
  - Confirm the 'Success' area appears indicatating that the specialized user
  has now been created
  - Record the value in the 'Access key ID' column
  as the `TF_VAR_AWS_DEVOPS_ACCESS_KEY_ID` variable value
  - Record the value in the 'Secret access key' column
  as the `TF_VAR_AWS_DEVOPS_SECRET_ACCESS_KEY` variable value
    - This is a secret value that shouldn't be shared with others

Note - There is no way to view the 'Secret access key' value again. The only other option
is to create another key/secret pair. If that's needed, do the following then update
the `TF_VAR_AWS_DEVOPS_ACCESS_KEY_ID`/`TF_VAR_AWS_DEVOPS_SECRET_ACCESS_KEY` variable values.
Since you've just created these values, you don't need to run these steps now but may
need to do so in the future.

  - Go to https://console.aws.amazon.com/iam/home and (if necessary) login
  with your normal Amazon username/password
  - Click the 'Users' link in the left column
  - Click the username link (should be the value as the `TF_VAR_AWS_DEVOPS_USERNAME` variable)
  under the 'User name' column
  - Click the 'Security credentials' tab
  - Under the 'Access keys' section, click the 'Create access key' button
  - Record the value in the 'Access key ID' column
  as the `TF_VAR_AWS_DEVOPS_ACCESS_KEY_ID` variable value
  - Click the 'Show' hyperlink
  - Record the value in the 'Secret access key' column
  as the `TF_VAR_AWS_DEVOPS_SECRET_ACCESS_KEY` variable value
    - This is a secret value that shouldn't be shared with others
  - Click the 'Close' button

##### Assign Appropriate Permissions to the Specialized DevOps User

After it's created, the specialized DevOps user can't do anything. Beyond the 'root'
account (that's the one that can login to Amazon too) which can do anything, other users
can't do anything unless they are explicitly assigned various rights to do specific
things within AWS.

In this section, you will be assigning appropriate permissions to the specialized DevOps
user so that it can install and maintain the web application.

This user needs various permissions including the following.
  - `DynamoDB` - Read from and write to the application's user information database table
  - `IAM` - Give the ability to create another user specifically for the web application
  and assign it permissions it needs to run the application
  - `S3` - Read and write files
  - `LightSail` - Create and manage a LightSail server with a static IP address
  - `Route53` - Add DNS records to route calls from the domain name
  (like 'https://chhcsfun.com') to the static IP address assigned to the LightSail instance

To add the correct permissions to this specialized DevOps user, do the following.

  - Go to https://console.aws.amazon.com/iam/home and (if necessary) login
  with your normal Amazon username/password
  - Click the 'Users' link in the left column
  - Click the username link (should be the value as the `TF_VAR_AWS_DEVOPS_USERNAME` variable)
  under the 'User name' column
  - In the 'Permissions' tab under the 'Permissions policies' section, click the
  'Add inline policy' link
  - Click the 'JSON' tab
  - Overwrite the contents in the text area with the contents found in the JSON block
  below, but make sure to study the content carefully and perform all variable substitutions
  - Click the 'Review policy' button
  - In the 'Name' field, enter `policies_for_user_#TF_VAR_AWS_DEVOPS_USERNAME#`
  - Click the 'Create policy' button


### S3 Permissions
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "One",
            "Effect": "Allow",
            "Action": [
                "s3:DeleteObject",
                "s3:GetObject",
                "s3:PutObject"
            ],
            "Resource": "arn:aws:s3:::#TF_VAR_AWS_S3_BUCKET_NAME_CONTENT#/*"
        },
        {
            "Sid": "OneAndHalf",
            "Effect": "Allow",
            "Action": [
                "s3:ListBucket",
                "s3:GetBucketPolicy",
                "s3:PutBucketPolicy"
            ],
            "Resource": "arn:aws:s3:::#TF_VAR_AWS_S3_BUCKET_NAME_CONTENT#"
        },
        {
            "Sid": "OneAndHalfAgain",
            "Effect": "Allow",
            "Action": [
                "s3:ListAllMyBuckets"
            ],
            "Resource": "arn:aws:s3:::*"
        }
    ]
}
```

### DynamoDB Permissions
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "Two",
            "Effect": "Allow",
            "Action": [
                "dynamodb:DescribeTable",
                "dynamodb:DescribeTimeToLive",
                "dynamodb:ListTagsOfResource",
                "dynamodb:DescribeContinuousBackups",
                "dynamodb:GetItem",
                "dynamodb:PutItem",
                "dynamodb:Scan",
                "dynamodb:UpdateItem",
                "dynamodb:Query",
                "dynamodb:GetRecords"
            ],
            "Resource": "arn:aws:dynamodb:#TF_VAR_AWS_REGION#:#TF_VAR_AWS_ACCOUNT_ID#:table/#TF_VAR_AWS_DYNAMODB_TABLE_NAME_USERAPPDATA#"
        }
    ]
}
```

### Route53 Permissions
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "Four",
            "Effect": "Allow",
            "Action": [
                "route53:ChangeResourceRecordSets",
                "route53:GetChange",
                "route53:GetHostedZone",
                "route53:ListHostedZones",
                "route53:ListResourceRecordSets",
                "route53:ListTagsForResource"
            ],
            "Resource": "*"
        }
    ]
}
```

### IAM Permissions
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "Five",
            "Effect": "Allow",
            "Action": [
                "iam:AttachUserPolicy",
                "iam:CreateAccessKey",
                "iam:CreatePolicy",
                "iam:CreateUser",
                "iam:DeleteAccessKey",
                "iam:DeletePolicy",
                "iam:DeleteUser",
                "iam:DetachUserPolicy",
                "iam:GetPolicy",
                "iam:GetPolicyVersion",
                "iam:GetUser",
                "iam:ListAccessKeys",
                "iam:ListEntitiesForPolicy",
                "iam:ListGroupsForUser",
                "iam:ListPolicyVersions"
            ],
            "Resource": "*"
        }
    ]
}
```

### LightSail Permissions
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "Six",
            "Effect": "Allow",
            "Action": [
                "lightsail:AllocateStaticIp",
                "lightsail:AttachStaticIp",
                "lightsail:CreateInstances",
                "lightsail:DeleteInstance",
                "lightsail:DetachStaticIp",
                "lightsail:GetInstance",
                "lightsail:GetOperation",
                "lightsail:GetStaticIp",
                "lightsail:ReleaseStaticIp"
            ],
            "Resource": "*"
        }
    ]
}
```

### Cognito Permissions
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "Seven",
            "Effect": "Allow",
            "Action": [
                "cognito-identity:ListIdentityPools"
            ],
            "Resource": "*"
        }
    ]
}
```

Cognito Reference - https://docs.aws.amazon.com/cognito/latest/developerguide/resource-permissions.html

##### Login to the AWS Console as the Specialized DevOps User

Bookmark Url
Use this user to login to the AWS console in the future
Use Incognito user

##### Create an Access Key for the Specialized DevOps User

login to console


#### Create Constrained User for This Application

While it is possible to use a regular Amazon or Amazon Prime user to login and use AWS,
that user can do _anything_ including quickly running up a large bill! Likewise, if a different
person is using the account (like a parent is allowing a child) then the other person
should be able to login to the AWS but shouldn't be able to login to regular Amazon to do
things like order books!

It's quite common (even standard practice) in computer systems to _Not_ use
the main ('all powerful') administrative user to get things done, but instead for that
user to create another user with just enough restricted access to only be able to do
what's needed in a given situation.

##### Create Constrained User

A user with constrained rights (only enough to create and use this application) will be
crated in this section. That user will have two ways of accessing AWS.

  - A traditional username and password to log into the AWS console web site.
  - An ID and secret pair for accessing AWS from the command line.
    - This is actually more important than the username and password combination. It
    allows automated setup of most of the application.

Generate user and get key-secret pair

  - Go to https://console.aws.amazon.com/iam/home?region=us-east-1#/users
  - Click the 'Add user' button
  - For 'User name' section enter `'chhcsfun'` in the text box
  - Under 'Access type' section check both 'Programmatic access' checkbox
  and 'AWS Management Console access' checkbox
  - Under 'Console password' section, select 'Custom password' radio button,
  check 'Show passsword' checkbox, then enter a password that you'll remember in the text box
  - Uncheck 'Require password reset' checkbox
  - Click the 'Next: permissions' button
  - Click the 'Next: Tags' button
  - Click the 'Next: Review' button
  - Click the 'Create User' button
  - Click the 'Download .csv' button
Get the login URL for this user by doing the following
  - Go to https://console.aws.amazon.com/iam/home?region=us-east-1#/home
  - Copy the URL just under the 'Sign-in URL for IAM users in this account' message
  - Open an Incognito window (not just another browser tab) and paste the URL into it
  - Bookmark the page
  - Try logging in using the user created above

Configure that user to use the `aws` command-line program.




//

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "One",
            "Effect": "Allow",
            "Action": [
                "s3:DeleteObject",
                "s3:GetObject",
                "s3:PutObject"
            ],
            "Resource": "arn:aws:s3:::#TF_VAR_AWS_S3_BUCKET_NAME_CONTENT#/*"
        },
        {
            "Sid": "OneAndHalf",
            "Effect": "Allow",
            "Action": [
                "s3:ListBucket",
                "s3:GetBucketPolicy",
                "s3:PutBucketPolicy"
            ],
            "Resource": "arn:aws:s3:::#TF_VAR_AWS_S3_BUCKET_NAME_CONTENT#"
        },
        {
            "Sid": "Two",
            "Effect": "Allow",
            "Action": [
                "dynamodb:DescribeTable",
                "dynamodb:DescribeTimeToLive",
                "dynamodb:ListTagsOfResource",
                "dynamodb:DescribeContinuousBackups"
            ],
            "Resource": "arn:aws:dynamodb:us-east-1:#TF_VAR_AWS_ACCOUNT_ID#:table/#TF_VAR_AWS_DYNAMODB_TABLE_NAME_USERAPPDATA#"
        },
        {
            "Sid": "Four",
            "Effect": "Allow",
            "Action": [
                "route53:ChangeResourceRecordSets",
                "route53:GetChange",
                "route53:GetHostedZone",
                "route53:ListHostedZones",
                "route53:ListResourceRecordSets",
                "route53:ListTagsForResource"
            ],
            "Resource": "*"
        },
        {
            "Sid": "Five",
            "Effect": "Allow",
            "Action": [
                "iam:AttachUserPolicy",
                "iam:CreateAccessKey",
                "iam:CreatePolicy",
                "iam:CreateUser",
                "iam:DeleteAccessKey",
                "iam:DeletePolicy",
                "iam:DeleteUser",
                "iam:DetachUserPolicy",
                "iam:GetPolicy",
                "iam:GetPolicyVersion",
                "iam:GetUser",
                "iam:ListAccessKeys",
                "iam:ListEntitiesForPolicy",
                "iam:ListGroupsForUser",
                "iam:ListPolicyVersions"
            ],
            "Resource": "*"
        },
        {
            "Sid": "Six",
            "Effect": "Allow",
            "Action": [
                "lightsail:AllocateStaticIp",
                "lightsail:AttachStaticIp",
                "lightsail:CreateInstances",
                "lightsail:DeleteInstance",
                "lightsail:DetachStaticIp",
                "lightsail:GetInstance",
                "lightsail:GetOperation",
                "lightsail:GetStaticIp",
                "lightsail:ReleaseStaticIp"
            ],
            "Resource": "*"
        }
    ]
}
```

### Prepare the Account as the Root User

The owner of the account (the one who pays the bills) should _not_ be the one
who uses the account to create and run the application. The owner can do anything
without restraint. Instead, the owner should create a special user with limited rights
to the account. Doing this lowers the likelihood of the following unwanted outcomes.

  - The owner of the account can also log into `https://amazon.com` . If the owner
  (like a parent) is different than the user deploying the application (like a child),
  this other user will need the owner's username/password combination to log into
  the AWS console (`https://aws.amazon.com/`). This will give the other user the
  ability to order things on Amazon while charging bills to the owner, even if the
  owner doesn't even know about it!
  - The owner of the account can do anything in AWS, including needlessly running up
  larger-than-necessary bills and using services that aren't required to get this
  application up and running. It's better to create a user which only has permissions
  to successfully install and use the application.



//

        {
            "Sid": "H",
            "Effect": "Allow",
            "Action": [
                "cognito-idp:ListUserPools"
            ],
            "Resource": "*"
        },
        {
            "Sid": "I",
            "Effect": "Allow",
            "Action": [
                "cognito-idp:Admin*",
                "cognito-idp:List*"
            ],
            "Resource": "arn:aws:cognito-idp:#TF_VAR_AWS_REGION#:#TF_VAR_AWS_ACCOUNT_ID#:userpool/#TF_VAR_AWS_COGNITO_USER_POOL_ID#"
        }


[Click here to go back to the main page.](../../README.md)
