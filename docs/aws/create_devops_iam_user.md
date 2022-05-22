# Create AWS Entity - Create `DevOps` IAM User

[Click here to go back to the main page.](../../README.md)

As mentioned, you should not use the same user (`Root User`) who pays the bills as the user
who installs and maintains the application.

Follow these steps to create this other specialized user (`DevOps User`).

## Record URL of Login Page for IAM Users (Like the `DevOps User`)

Users created by the `Root User` _cannot_ login on the general AWS login page
(https://aws.amazon.com/). Instead, these users must login to a special login page
in order to access the AWS console.

The URL of that login page is as follows. Record it as the `TF_VAR_AWS_DEVOPS_CONSOLE_URL`
variable value.

```
https://#TF_VAR_AWS_ACCOUNT_ID#.signin.aws.amazon.com/console/
```

Reference - https://docs.aws.amazon.com/IAM/latest/UserGuide/console_account-alias.html


## Create the Specialized DevOps User Which Will Be Used to Install and Maintain the App

  - Go to https://aws.amazon.com and login as you would on normal Amazon (`Root User`)
  - In the search box, enter 'IAM' and select the 'IAM' result
  - In the left column, click the 'Users' link
  - Click the 'Add user' button
  - In the 'User name' text box, enter a username of your choice (like `devops_user`)
    - Record this value as the `TF_VAR_AWS_DEVOPS_USERNAME` variable value
  - Under 'Access type,' select both 'Progammatic access' (from `aws` tool)
  and 'AWS Management Console access' (from browser) options
  - Under the 'Console password' section, select the 'Custom password' option
  then enter a password of your choosing
    - Record this value as the `TF_VAR_AWS_DEVOPS_PASSWORD` variable value
    - This is a secret value that shouldn't be shared with others
  - Uncheck the 'Require password reset' option
  - Click the 'Next: Permissions' button
  - For now, ignore adding permissions and instead click the 'Next: Tags' button
  - Click the 'Next: Review' button
  - Click the 'Create user' button
  - Confirm the 'Success' area appears indicatating that the `DevOps User`
  has now been created
  - Record the value in the 'Access key ID' column
  as the `TF_VAR_AWS_DEVOPS_ACCESS_KEY_ID` variable value
  - Record the value in the 'Secret access key' column
  as the `TF_VAR_AWS_DEVOPS_SECRET_ACCESS_KEY` variable value
    - Click the 'Show' link to make this value visible
    - This is a secret value that shouldn't be shared with others

### If Necessary, Steps to Recreate the Key/Secret Pair in the Future

_Since you've just created these values, you don't need to run these steps now but may
need to do so in the future. Skip this section but know where these instructions are
in case they are needed latter._

Note - There is no way to view the 'Secret access key' value again. The only other option
is to create another key/secret pair. If that's needed, do the following then update
the `TF_VAR_AWS_DEVOPS_ACCESS_KEY_ID`/`TF_VAR_AWS_DEVOPS_SECRET_ACCESS_KEY` variable values.

Here are the steps to recreate the key/secret pair.

  - Go to https://console.aws.amazon.com/iam/home and (if necessary) login as the `Root User`
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

### Confirm that the `DevOps User` Can Login to the AWS Console

In an incognito browser, go to URL `#TF_VAR_AWS_DEVOPS_CONSOLE_URL#`
and login using `#TF_VAR_AWS_DEVOPS_USERNAME#` as the username
and `#TF_VAR_AWS_DEVOPS_PASSWORD#` as the password.

### Configure `aws` Command-Line Tool

Configure the `aws` command-line tool so that it executes commands at the `DevOps User`.
Open the command prompt and execute the following (3) commands.

```
aws configure set aws_access_key_id "#TF_VAR_AWS_DEVOPS_ACCESS_KEY_ID#"
aws configure set aws_secret_access_key "#TF_VAR_AWS_DEVOPS_SECRET_ACCESS_KEY#"
aws configure set default.region "#TF_VAR_AWS_REGION#"
```

On Mac, these entries should now appear in the '\~/.aws/credentials' and the '\~/.aws/config' files.


### Assign Appropriate Permissions to the `DevOps User`

After it's created, the specialized `DevOps User` can't do anything. Beyond the `Root User`
which can do anything, other users can't do anything unless they are explicitly assigned
various rights to do specific things within AWS.

In this section, you will be assigning appropriate permissions to the specialized `DevOps User`
so that it can install and maintain the web application.

This user needs various permissions including the following.
  - `S3` - Read and write files
  - `DynamoDB` - Read from and write to the application's user information database table
  - `Route53` - Add DNS records to route calls from the domain name
  (like 'https://chhcsfun.com') to the static IP address assigned to the LightSail instance
  - `IAM` - Give the ability to create another user specifically for the web application
  and assign it permissions it needs to run the application
  - `LightSail` - Create and manage a LightSail server with a static IP address
  - `Cognito` - Manage external user who can login to the application

To add the correct permissions to this specialized `DevOps User`, do the following.

  - Go to https://console.aws.amazon.com/iam/home and (if necessary) login
  with your normal Amazon username/password (`Root User`)
  - Click the 'Users' link in the left column
  - Click the username link (should be the value as the `TF_VAR_AWS_DEVOPS_USERNAME` variable)
  under the 'User name' column

Now that you're on the `DevOps User` profile page, do the following for each subsection below
(each one's title starting with 'Permissions -'). _Many of these sections require variable
substitutions - carefully make these substitutions and confirm that the '#' characters have
been removed._
  - In the 'Permissions' tab under the `Permissions policies` section, click the
  `Add inline policy` link
  - Click the `JSON` tab
  - Overwrite the contents in the text area with the contents found in the JSON block
  of the subsection, but make sure to study the content carefully and perform
  all variable substitutions
  - Click the 'Review policy' button
  - In the 'Name' field, enter the 'Policy Name' value listed in the subsection
  - Click the 'Create policy' button


#### Permissions - S3

  - Policy Name - `policies_for_s3`

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "A",
            "Effect": "Allow",
            "Action": [
                "s3:DeleteObject",
                "s3:Get*",
                "s3:PutObject"
            ],
            "Resource": "arn:aws:s3:::#TF_VAR_AWS_S3_BUCKET_NAME_CONTENT#/*"
        },
        {
            "Sid": "B",
            "Effect": "Allow",
            "Action": [
                "s3:ListBucket",
                "s3:Get*",
                "s3:PutBucketPolicy"
            ],
            "Resource": "arn:aws:s3:::#TF_VAR_AWS_S3_BUCKET_NAME_CONTENT#"
        },
        {
            "Sid": "C",
            "Effect": "Allow",
            "Action": [
                "s3:ListAllMyBuckets"
            ],
            "Resource": "arn:aws:s3:::*"
        }
    ]
}
```

#### Permissions - DynamoDB

  - Policy Name - `policies_for_dynamodb`

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "A",
            "Effect": "Allow",
            "Action": [
                "dynamodb:List*",
                "dynamodb:Describe*"
            ],
            "Resource": "*"
        },
        {
            "Sid": "B",
            "Effect": "Allow",
            "Action": [
                "dynamodb:Get*",
                "dynamodb:PutItem",
                "dynamodb:Scan",
                "dynamodb:UpdateItem",
                "dynamodb:Query"
            ],
            "Resource": "arn:aws:dynamodb:*:*:table/#TF_VAR_AWS_DYNAMODB_TABLE_NAME_USERAPPDATA#"
        }
    ]
}
```

#### Permissions - Route53

  - Policy Name - `policies_for_route53`

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "A",
            "Effect": "Allow",
            "Action": [
                "route53:ChangeResourceRecordSets",
                "route53:Get*",
                "route53:List*"
            ],
            "Resource": "*"
        }
    ]
}
```

#### Permissions - IAM

  - Policy Name - `policies_for_iam`

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "A",
            "Effect": "Allow",
            "Action": [
                "iam:AttachUserPolicy",
                "iam:CreateAccessKey",
                "iam:CreatePolicy",
                "iam:CreateUser",
                "iam:DeleteAccessKey",
                "iam:DeletePolicy",
                "iam:DeletePolicyVersion",
                "iam:DeleteUser",
                "iam:DetachUserPolicy",
                "iam:Get*",
                "iam:List*"
            ],
            "Resource": "*"
        }
    ]
}
```

#### Permissions - LightSail

Important Note - Add policy that will allow DevOps user to reboot server.
https://lightsail.aws.amazon.com/ls/docs/en_us/articles/security_iam_resource-based-policy-examples


  - Policy Name - `policies_for_lightsail`

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "A",
            "Effect": "Allow",
            "Action": [
                "lightsail:AllocateStaticIp",
                "lightsail:AttachStaticIp",
                "lightsail:CreateInstances",
                "lightsail:DeleteInstance",
                "lightsail:DetachStaticIp",
                "lightsail:Get*",
                "lightsail:ReleaseStaticIp",
                "lightsail:RebootInstance",
                "lightsail:StartInstance",
                "lightsail:StopInstance"
            ],
            "Resource": "*"
        }
    ]
}
```

#### Permissions - Cognito

  - Policy Name - `policies_for_cognito`

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "A",
            "Effect": "Allow",
            "Action": [
                "cognito-idp:Describe*",
                "cognito-idp:List*"
            ],
            "Resource": "*"
        },
        {
            "Sid": "B",
            "Effect": "Allow",
            "Action": [
                "cognito-idp:Admin*"
            ],
            "Resource": "arn:aws:cognito-idp:*:*:userpool/#TF_VAR_AWS_COGNITO_USER_POOL_ID#"
        }
    ]
}
```

Cognito Reference - https://docs.aws.amazon.com/cognito/latest/developerguide/resource-permissions.html



# Ignore This Section

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
