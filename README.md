# chhcsfun_template

## Goals

The goal of this project is to give you the ability to host an application server in AWS
with support for HTTPS, user login, and per-user data storage.

This application server can also be run on your local machine with the option of either
simulating ('mocking') interaction with AWS or actually integrating with AWS.

AWS has a vast portfolio of categories of services. Everything from renting computer time,
storing data, identifying faces in pictures, crowdsourcing the talents of other people
('Mechnical Turk'), and so much more can be done with AWS.

Here are the categories of services that will be used by this application. Note that
if you decide to run the application locally and integrate with AWS, you should (in theory)
be able to avoid being charged (as of April 2021). All estimated costs listed here are
as of April 2021.

  - `LightSail` - This is a lower-cost computing rental service meant to give you a dedicated
  (Linux) machine to host the application server. This computer is assigned a 'dedicated'
  (fixed) IP address which makes mapping it to a domain name (like 'chhcsfun.com')
  real easy.
    - When Needed - Only when hosting app server on AWS, not when running app server locally
    - Estimated Cost - $3.50 per month
  - `Route53` - This is used to claim a domain name (like 'chhcsfun.com') for your use and
  map it to the 'dedicated' IP address of the LightSail machine which will allow it to
  interact with web browsers when a URL containing the domain name is included
  (like 'https://chhcsfun.com')
    - When Needed - Only when hosting app server on AWS, not when running app server locally
    - Estimated Cost - $12 per year to claim the domain name, $0.50 per month to map this
    domain name to the 'dedicated' IP assigned to the LightSail machine
  - `S3` - This is a cloud-based file system for storing the files that your web application
  will send ('serve') to browsers. There are two advantages of using this service instead
  of storing the files here - (A) updating these files is easy versus trying to update the
  files running within LightSail and (B) these files can be versioned while also being
  retained even after the LightSail server is destroyed (e.g. to save money).
    - When Needed - Both when hosting app server on AWS or locally but integrated with AWS,
    not used in local simulated ('mocked') mode
    - Estimated Cost - Free
  - `IAM` - This is a user-management service which (among other things) allows the root
  user (the one paying the bills) create other users who (usually) have limited rights
  (so they can't do too much damage), note that in this context 'users' means those
  using AWS (not the web application)
    - When Needed - Both when hosting app server on AWS or locally but integrated with AWS,
    not used in local simulated ('mocked') mode
    - Estimated Cost - Free
  - `Cognito` - This allows users of the web application (not AWS, which is what IAM is for)
  to create user accounts with simple information (like the user's display name) to be assigned
  to them, integration with third-parties (like GMail, using something called 'OAuth2')
  is possible (which allows a user to login to the application without having to create
  another username/password combination)
    - When Needed - Both when hosting app server on AWS or locally but integrated with AWS,
    not used in local simulated ('mocked') mode
    - Estimated Cost - Free
  - `DynamoDB` - This is a ('NoSQL') database service which can be used to store a user's data
  for a specific application
    - When Needed - Both when hosting app server on AWS or locally but integrated with AWS,
    not used in local simulated ('mocked') mode
    - Estimated Cost - Free

These specific (free) tools will be used to complete the installation.

  - `aws` - This is the AWS command-line tool that allows your local machine (and the LightSail
  server too) to interact with AWS, note that programs which run AWS commands will rely on the
  configurations made by this program to access AWS (which makes this tool critical for AWS
  integration)
    - When Needed - Both when hosting app server on AWS or locally but integrated with AWS,
    not used in local simulated ('mocked') mode
  - `terraform` - Terraform is a popular (arguably vital) program for automating the
  construction and (importantly) destruction of entities within a cloud service (like AWS),
  this is often a cornerstone tool in the `Infrastructure-As-Code` methodology
    - When Needed - Only when hosting app server on AWS, not when running app server locally

## Billing

This setup involves using AWS resources, the usage of some of which
(e.g. Route53, LightSail) will incur charges billed monthly.

### Viewing Billing

To view the accumulated charges for this month go to
https://console.aws.amazon.com/billing/home#/ . On that page, click the 'Bills' link
to view the bills for previous months.

### Create Daily and Monthly Budgets to Send Email Alerts If Too Much Money Is Being Spent

AWS supports the creation of budgets which can send alerts when more than a certain amount
of money is being charged over a period of time. Create two reports - daily and monthly -
to catch surprise expenditures early.

These two reports are recommended because the registration or reregistration of a domain
name could cause a spike both in daily and monthly expenses that just lasts a day.
Having these two reports should allow both detection of a one-time spike in costs as
well as a higher-than-normal accumulation of expenses over a longer period (i.e. month).

Follow these instructions for each of the (2) reports - 'Daily' and 'Monthly'.

  - Go to https://console.aws.amazon.com/billing/home
  - Click the 'Budgets' link in the left column
  - Click the 'Create a budget' button on the right
  - Keep the 'Cost budget' radio button selected then click the 'Set your budget' button
  - Enter a value in the 'Name' field
    - Daily - CHHCS Fun App Daily Budget
    - Monthly - CHHCS Fun App Monthly Budget
  - Select a value in the 'Period' dropdown box
    - Daily - Daily
    - Monthly - Monthly
  - Enter a value in the 'Budgeted amount' field (these are example recommended values)
    - Daily - 15
    - Monthly - 20
  - Click the 'Configure thresholds' button located in the bottom right
  - In the 'Alert threshold' text box, enter 100
  - In the 'Email recipients' text area, enter a comma-separated list of the email
  addresses that should receive an alert
  - Click the 'Confirm budget' button in the lower right
  - Review your settings then click the 'Create' button in the lower right

## Phases

### Phase 0 - Select an Unused Domain Name and Get Ready to Record Variable Values

#### Select an Unused Domain Name

Think of the domain name (like 'chhcsfun.com' for your web site). You don't need to actually register
(and pay for) a domain name right now. You will only need to register a domain name once you've
decided to host your web application on a LightSail server. _A domain name isn't needed to run
the web application locally but the AWS entities (like the DynamoDB table and Cognito entities)
will be assigned names that contain the domain name so correctly identifying the desired domain name
now will allow for migration of the local web application into the cloud._

Note that your selection of a domain name here (obviously) doesn't mean that you have formally
registered it in AWS. You only register (temporarily own on a yearly basis) a domain and are charged
by AWS for that registration by following these steps (which can also be used to determine if a domain
name is available without actually reserving it).

  - Go to https://console.aws.amazon.com/route53/home
  - Click the 'Domains' link
  - Click the 'Register Domain' button
  - Enter your desired domain name into the text box, select your desired extension (which is
  usually just '.com'), then click the 'Check' button
    - View the results to find out whether the domain name is available
    - If your goal is to just find out whether the domain name is available without actually
    reserving it (and paying for it) right now, just click the 'Cancel' link and stop here
    without continuing
  - If the domain name is available, click the 'Add to cart' button then the 'Continue' button
  - Fill in your contact information, then click the 'Continue' button
    - Once you reserve the domain, much of this information is available to the public unless
    you click the 'Enable' radio button under the 'Privacy Protection' section (doing this
    _should_ hide most of the information from the public)
  - Under the 'Do you want to automatically renew your domain?' section, consider whether
  you want your registration of this domain name to automatically renew once it expires,
  (obviously) auto-renewal will charge your account upon arriving on the renewal date
    - When in doubt, just select the 'Disable' option which means that you will lose
    the reservation for the domain name once its term expires _But_ this will also mean
    that you won't receive reoccurring charges for renewals
    - Once a reservation expires, _anyone_ can register the domain and (for unknown reasons)
    the cost to reserve that domain name can go up significantly (even 10 times or more
    the original reservation cost)
  - Click the checkbox under the 'Terms and Conditions' section
  - Click the 'Complete Order' button

Record the domain name as it will be referenced as `#DOMAIN_NAME#` subsequently in these
instructions. This variable is listed as `DOMAIN_NAME` in the `List of Variable Values`
section below.

##### Record the AWS Account ID

In the same browser, do the following.

  - Click your name in the top left and record the number next to 'My Account'

It will be referenced as `#AWS_ACCOUNT_ID#` subsequently in these instructions.
This variable is listed as `AWS_ACCOUNT_ID` in the `List of Variable Values` section below.

##### Always Use the `us-east-1` AWS Region

AWS hosts its services all around the world in well-defined regions. It's important
to use the same region when you're creating the AWS components for an application.
(Note that some components, like S3 'buckets' and IAM users are 'global' in that they
aren't assigned to specific regions. They are region-less.)

For this setup, use the `us-east-1` region. Ensure this setting by doing the following.

  - Go to any AWS page like https://console.aws.amazon.com/console/home
  - Just right of your name in the top right, click the dropdown and select the
  `US East (N. Virginia) us-east-1` value

#### Get Ready to Record Variable Values

There are several values listed in these instructions which will only be known by you.
These values cannot be determined ahead of time. As these values come up, record them
in a common table that you can reference later.

##### Representation of Variable Values

When these values come up for you to enter, they will appear as their variable name
and surrounded by the `#` character. So if the variable name `DOMAIN_NAME` has a value
of `chhcsfun.com`, then when you're asked to enter a statement like the following.

```
export TF_VAR_DOMAIN_NAME="#DOMAIN_NAME#"
```

Enter the following instead.

```
export TF_VAR_DOMAIN_NAME="chhcsfun.com"
```

##### List of Variable Values

Write down (either (preferrably) electronically or on paper) a table consisting of two columns.
The left column should be filled with the variable names listed below and in the right
column write the value of that variable as you discover it. _Note that some of these values
should be kept secret so don't share this list with others._

  - `DOMAIN_NAME` - The hostname (like 'chhcsfun.com') of the web site
  - `AWS_ACCOUNT_ID` - This is the unique number assigned to your AWS account
  - `AWS_REGION` - This should have a value of `us-east-1`
  - `AWS_DEVOPS_USERNAME` - The username of the specialized account used to access AWS
  - `AWS_DEVOPS_PASSWORD` - The password of the specialized account used to access AWS
  - `AWS_CUSTOM_LOGIN_URL` - This is the AWS account-specific login URL
  - `AWS_DEVOPS_ACCESS_KEY_ID` - This is the equivalent of a username for `aws` tool
  usage for the specialized account
  - `AWS_DEVOPS_SECRET_ACCESS_KEY` - This is the equivalent of a password for `aws` tool
  usage for the specialized account
  - `AWS_S3_BUCKET_NAME_CONTENT` - The name of the S3 bucket
  - `AWS_DYNAMODB_TABLE_NAME_USERAPPDATA` - The name of the DynamoDB table
  - `AWS_COGNITO_CLIENT_ID` - The client ID (like a username) to access Cognito
  - `AWS_COGNITO_CLIENT_SECRET` - The client secret (like a password) to access Cognito
  - `AWS_COGNITO_DOMAIN_NAME` - The base URL for your application's login capabilities
  - `AWS_COGNITO_OAUTH2_AUTHORIZE_URL` - The login URL for users using your application
  - `AWS_COGNITO_OAUTH2_TOKEN_URL` - The URL your application accesses to confirm a user login
  - `AWS_COGNITO_USER_POOL_ID` - The AWS ID assigned to the pool of users of your application
  - `AWS_DEVOPS_CONSOLE_URL` - https://docs.aws.amazon.com/IAM/latest/UserGuide/console_account-alias.html

### Phase 1 - Local Server with Simulated Calls to AWS

Follow the instructions at
https://github.com/garysteinmetz/technology_survey_study_materials/blob/master/lessons/bonus_i_have_used_the_online_authoring_tool_to_create_a_simple_game.md .

### Phase 2 - Local Server Integrated with AWS

#### Create AWS Entities for Application

##### Create S3 Bucket

First, create the bucket.

  - Go to https://aws.amazon.com and login as you would on normal Amazon
    - The browser should go to https://console.aws.amazon.com/console/home
  - In the search box, enter 'S3' and select the 'S3' result
  - Click the 'Create bucket' button
  - Under 'Bucket name' enter 'content.#DOMAIN_NAME#'
  - Under the 'Block Public Access settings for this bucket' section,
  uncheck the 'Block all public access' checkbox and ensure all sub-checkboxes
  under it are unchecked too
  - Check the 'I acknowledge ...' checkbox just under that section
  - Scroll to the bottom of the page and click 'Create bucket' button
  - Record the name of this bucket as the `AWS_S3_BUCKET_NAME_CONTENT` variable value

Now, allow the possibility of general access to this bucket. Note that this AWS account
will still need to grant separate access to each user.

  - You should now be back on the main S3 page
  ( https://s3.console.aws.amazon.com/s3/home )
  - Scroll down the table and click the `#AWS_S3_BUCKET_NAME_CONTENT#` link under
  the 'Name' column
  - Click the 'Permissions' tab
  - Scroll down to the 'Bucket policy' section and click the 'Edit' button
  - In the text area under the 'Policy' section, enter the following then click
  the 'Save changes' button
    - Make sure to substitute `#AWS_S3_BUCKET_NAME_CONTENT#`

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
            "Resource": "arn:aws:s3:::#AWS_S3_BUCKET_NAME_CONTENT#/*"
        },
        {
            "Sid": "Stmt1617405350836",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:*",
            "Resource": "arn:aws:s3:::#AWS_S3_BUCKET_NAME_CONTENT#"
        }
    ]
}
```

##### Create DynamoDB Table

  - Go to https://aws.amazon.com and login as you would on normal Amazon
    - The browser should go to https://console.aws.amazon.com/console/home
  - In the search box, enter 'DynamoDB' and select the 'DynamoDB' result
  - Click the 'Create table' button
  - Click the 'Tables' link in the left column
  - Click the 'Create table' button
  - In the 'Table name' field, enter 'userAppData.#DOMAIN_NAME#'
  - In the 'Partition key' field, enter 'app_id'
  - Check the 'Add sort key' checkbox and enter 'user_id' in field just below it
  - Scroll down the page and click the 'Create' button
  - Record the table name as the `AWS_DYNAMODB_TABLE_NAME_USERAPPDATA` variable value

##### Create Cognito User Pool

  - Go to https://aws.amazon.com and login as you would on normal Amazon
    - The browser should go to https://console.aws.amazon.com/console/home
  - Make sure you have selected the correct AWS region (like 'US East (N. Virginia) us-east-1')
  - In the search box, enter 'Cognito' and select the 'Cognito' result
  - Click the 'Manage User Pools' button
  - Click the 'Create a user pool' button
  - Enter '#DOMAIN_NAME#' for the name of the user pool
  - Click the 'Review defaults' button
  - Scroll to the bottom of the page and click the 'Create pool' button
  - In the resulting page, record the 'Pool Id' value at the top
  as the `AWS_COGNITO_USER_POOL_ID` variable value
  - Click the 'App clients' link in the left column
  - Click the 'Add an app client' button
  - Enter '#DOMAIN_NAME#' in the 'App client name' field
  - Scroll down to the bottom of the page and click the 'Create app client' button
  - In the resulting page, click the 'Show Details' button
  - Under the 'App client id' section, record the value
  as the `AWS_COGNITO_CLIENT_ID` variable value
  - Under the 'App client secret' section, record the value
  as the `AWS_COGNITO_CLIENT_SECRET` variable value
  - Click the 'App client settings' link on the left side
  - In the 'Enabled Identity Providers' section, check the 'Select all' checkbox
  - In the 'Callback URL(s)' text box, enter the following
    - `http://localhost:8080/oauthTwoCallback, https://#DOMAIN_NAME#/oauthTwoCallback`
  - Under the 'Allowed OAuth Flows' section, check the 'Authorization code grant' checkbox
  - Check all checkboxes under the 'Allowed OAuth Scopes' section
  - Click the 'Save changes' button
  - Click the 'Domain name' link on the left side
  - In the 'Domain prefix' text box, enter '#DOMAIN_NAME#' but replace any '.' (period)
  with a '-' (hyphen)
  - Click the 'Check availability' button to confirm that that login domain name is available
  - Click the 'Save changes' button
  - Record the full URL (including 'https://' prefix and '.com' suffix)
  in the 'Amazon Cognito domain' section as the `AWS_COGNITO_DOMAIN_NAME` variable value

Using the `AWS_COGNITO_DOMAIN_NAME` variable value, derive these variable values.

  - `AWS_COGNITO_OAUTH2_AUTHORIZE_URL`
    - `#AWS_COGNITO_DOMAIN_NAME#/oauth2/authorize?response_type=code&client_id=#AWS_COGNITO_CLIENT_ID#`
  - `AWS_COGNITO_OAUTH2_TOKEN_URL`
    - `#AWS_COGNITO_DOMAIN_NAME#/oauth2/token`

#### Install `aws` Command-Line Tool

##### Set Up Local `aws` Configurations

`aws` is the command-line program that allows you to read the state of and update
the configurations to AWS. The functionality of this program nearly matches one-for-one
what can be done on the AWS website. While a `Terraform` script will be doing the automated
setup part of the installation, it uses the `aws` configurations stored in the
`~/.aws/credentials` file. Likewise, having the `aws` program ready-to-use is often
necessary for a cloud developer.

###### Install `aws` on Windows

On Windows, download the file at https://awscli.amazonaws.com/AWSCLIV2.msi
and install it onto your Desktop, then run and install it.

Reference - https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2-windows.html

###### Install `aws` on Mac

On Mac, open a command prompt, go to the Desktop, run the following command, then
open and run the file that gets downloaded to the Desktop.

```
curl "https://awscli.amazonaws.com/AWSCLIV2.pkg" -o "AWSCLIV2.pkg"
```

Reference - Step 3 of https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2-mac.html#cliv2-mac-install-cmd-current-user

#### Create a User and Configure the `aws` Tool to Perform Actions as That User

As mentioned, you should not use the same ('root') user who pays the bills as the user
who installs and maintains the application.

Follow these steps to create this other (specialized 'DevOps') user.

##### Create the Specialized DevOps User Which Will Be Used to Install and Maintain the App

  - Go to https://aws.amazon.com and login as you would on normal Amazon
  - In the search box, enter 'IAM' and select the 'IAM' result
  - In the left column, click the 'Users' link
  - In the 'User name' text box, enter a username of your choice (like 'chhcsfun')
  - Under 'Access type,' select both 'Progammatic access' (from `aws` tool)
  and 'AWS Management Console access' (from browser) options
    - Record this value as the `AWS_DEVOPS_USERNAME` variable value
  - Under the 'Console password' section, select the 'Custom password' option
  then enter a password of your choosing
    - Record this value as the `AWS_DEVOPS_PASSWORD` variable value
    - This is a secret value that shouldn't be shared with others
  - Uncheck the 'Require password reset' option
  - Click the 'Next: Permissions' button
  - For now, ignore adding permissions and instead click the 'Next: Tags' button
  - Click the 'Next: Review' button
  - Click the 'Create user' button
  - Confirm the 'Success' area appears indicatating that the specialized user
  has now been created
  - Record the value in the 'Access key ID' column
  as the `AWS_DEVOPS_ACCESS_KEY_ID` variable value
  - Record the value in the 'Secret access key' column
  as the `AWS_DEVOPS_SECRET_ACCESS_KEY` variable value
    - This is a secret value that shouldn't be shared with others

Note - There is no way to view the 'Secret access key' value again. The only other option
is to create another key/secret pair. If that's needed, do the following then update
the `AWS_DEVOPS_ACCESS_KEY_ID`/`AWS_DEVOPS_SECRET_ACCESS_KEY` variable values.
Since you've just created these values, you don't need to run these steps now but may
need to do so in the future.

  - Go to https://console.aws.amazon.com/iam/home and (if necessary) login
  with your normal Amazon username/password
  - Click the 'Users' link in the left column
  - Click the username link (should be the value as the `AWS_DEVOPS_USERNAME` variable)
  under the 'User name' column
  - Click the 'Security credentials' tab
  - Under the 'Access keys' section, click the 'Create access key' button
  - Record the value in the 'Access key ID' column
  as the `AWS_DEVOPS_ACCESS_KEY_ID` variable value
  - Click the 'Show' hyperlink
  - Record the value in the 'Secret access key' column
  as the `AWS_DEVOPS_SECRET_ACCESS_KEY` variable value
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
  - Click the username link (should be the value as the `AWS_DEVOPS_USERNAME` variable)
  under the 'User name' column
  - In the 'Permissions' tab under the 'Permissions policies' section, click the
  'Add inline policy' link
  - Click the 'JSON' tab
  - Overwrite the contents in the text area with the contents found in the JSON block
  below, but make sure to study the content carefully and perform all variable substitutions
  - Click the 'Review policy' button
  - In the 'Name' field, enter 'policies_for_user_#AWS_DEVOPS_USERNAME#'
  - Click the 'Create policy' button

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
            "Resource": "arn:aws:s3:::#AWS_S3_BUCKET_NAME_CONTENT#/*"
        },
        {
            "Sid": "OneAndHalf",
            "Effect": "Allow",
            "Action": [
                "s3:ListBucket",
                "s3:GetBucketPolicy",
                "s3:PutBucketPolicy"
            ],
            "Resource": "arn:aws:s3:::#AWS_S3_BUCKET_NAME_CONTENT#"
        },
        {
            "Sid": "OneAndHalfAgain",
            "Effect": "Allow",
            "Action": [
                "s3:ListAllMyBuckets"
            ],
            "Resource": "arn:aws:s3:::*"
        },
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
            "Resource": "arn:aws:dynamodb:#AWS_REGION#:#AWS_ACCOUNT_ID#:table/#AWS_DYNAMODB_TABLE_NAME_USERAPPDATA#"
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

##### Login to the AWS Console as the Specialized DevOps User

Bookmark Url
Use this user to login to the AWS console in the future
Use Incognito user

##### Create an Access Key for the Specialized DevOps User

login to console

##### Test the `aws` Command-Line Tool

### Phase 3 - LightSail Server Integrated with AWS

## Incremental Development

```
aws s3 sync --acl public-read --exclude ".*" ./content s3://#S3_BUCKET_NAME#/content
```

## Stop AWS Usage

### The Easy Way (Nuclear Bomb)

`To Close Your Account` - Follow the instructions under the `Close your account`
section of https://aws.amazon.com/premiumsupport/knowledge-center/close-aws-account/ .

  - Positive - This is easy and it could guagentee that you immediately don't
  incur any additional charges for using AWS.
  - Negative - This will delete everything, including all user accounts in Cognito
  and any accumulated data in DynamoDB. Renewal of your registered domain will be
  turned off.
    - Reference - https://docs.aws.amazon.com/Route53/latest/DeveloperGuide/troubleshooting-account-closed.html

After closing the AWS account, you have up to 90 days to reopen it by following
the instructions at https://aws.amazon.com/premiumsupport/knowledge-center/reopen-aws-account/ .
_After 90 days, you will permanently no longer be able to use AWS with that email account._

### The Hard Way (Surgical Strike)

#### Delete LightSail Server

#### Delete Route53 Domain (Auto-Renew)

#### Delete Route53 Zone

#### (Optionally) Delete Cognito Group

You shouldn't be charged by keeping this and it can be reused again.

#### (Optionally) Delete DynamoDB Table

You shouldn't be charged by keeping this and it can be reused again.

#### (Optionally) Delete S3 Bucket

You shouldn't be charged by keeping this and it can be reused again.

### (Optionally) Delete Billing Budgets


Create Billing Report

<details>
  <summary>Here is a list of what to do</summary>
  ### Instructions

  - Define Goal
  - List Steps
  - Execute
</details>

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

## Install and Configure the Server

### Pre-Installation Setup

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
  - For 'User name' section enter 'chhcsfun' in the text box
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

###### Set Up Local `aws` Configurations

`aws` is the command-line program that allows you to read the state of and update
the configurations to AWS. The functionality of this program nearly matches one-for-one
what can be done on the AWS website. While a `Terraform` script will be doing the automated
setup part of the installation, it uses the `aws` configurations stored in the
`~/.aws/credentials` file. Likewise, having the `aws` program ready-to-use is often
necessary for a cloud developer.

####### Install `aws` on Windows

On Windows, download the file at https://awscli.amazonaws.com/AWSCLIV2.msi
and install it onto your Desktop, then run and install it.

Reference - https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2-windows.html

####### Install `aws` on Mac

On Mac, open a command prompt, go to the Desktop, run the following command, then
open and run the file that gets downloaded to the Desktop.

```
curl "https://awscli.amazonaws.com/AWSCLIV2.pkg" -o "AWSCLIV2.pkg"
```

Reference - Step 3 of https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2-mac.html#cliv2-mac-install-cmd-current-user

####### Configure `aws`

Now configure the `aws` program with the credentials of the user you created earlier.
Open the file for that user you downloaded earlier (it should have a name like
'new_user_credentials.csv'). Run the commands below and substitute the values
'<ACCESS_KEY_ID>' and '<SECRET_ACCESS_KEY>' with the values in the 'Access key ID'
and 'Secret access key' columns, respectively.

```
aws configure set aws_access_key_id "<ACCESS_KEY_ID>"
aws configure set aws_secret_access_key "<SECRET_ACCESS_KEY>"
aws configure set default.region us-east-1
```

These entries should now appear in the '~/.aws/credentials' file.

###### Install Terraform

`Terraform` is a programming language (really a declarative language) for defining,
creating, and deleting entities in the cloud, especially AWS.

Go to https://www.terraform.io/downloads.html , download the version of Terraform matching
the OS of your machine onto your Desktop, then install it.

###### Grant User Limited Permissions (Policies)

Right now, the user cannot do anything (or really even see anything) in the AWS console.
To do anything, that user must be granted permissions (known as `polities` in AWS)
in order to do anything. Add polities to the user so that it has enough privileges
to administer and maintain the application.

Create S3 bucket with format 'content.<DOMAIN_NAME_HERE>'
Uncheck 'Block all public access', make sure everything is unchecked
Add the following bucket policy to it
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
            "Resource": "arn:aws:s3:::content.chhcsfun.com/*"
        },
        {
            "Sid": "Stmt1617405350836",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:*",
            "Resource": "arn:aws:s3:::content.chhcsfun.com"
        }
    ]
}
```
Create a 'content' subdirectory in this bucket and place an 'index.html' file in it
Run `aws configure` and enter values
Register hosted zone at https://console.aws.amazon.com/route53/v2/hostedzones

Go to the `_tf` subdirectory
Run `terraform init`
Run `terraform apply`

Set environment variables

Reference - https://certbot.eff.org/lets-encrypt/ubuntufocal-apache

```
curl http://localhost
curl http://localhost:8080
sudo snap install core
sudo snap refresh core
sudo apt-get remove certbot
sudo snap install --classic certbot
sudo ln -s /snap/bin/certbot /usr/bin/certbot
sudo certbot --apache
  - Enter email address
  - Enter 'Y' to agree with the 'Terms of Service'
  - Determine whether you'd like to receive email from the EFF org
  - Enter the root domain then a comma then the 'www.' subdomain
  - Enter '2' to select the '000-default-le-ssl.conf' virtual host for the 'www.' subdomain
Wait a few minutes then try to access the 'https' version of the URL (including 'www.')

sudo a2enmod proxy_http
sudo a2enmod headers

sudo service apache2 stop
sudo vi /etc/apache2/sites-available/000-default-le-ssl.conf
  - Put the following just above the '</VirtualHost>' end tag at the bottom
    - Replace '<DOMAIN_NAME_HERE>' with the correct domain name
ProxyPass / http://127.0.0.1:8080/
RequestHeader set X-Forwarded-Proto https
RequestHeader set X-Forwarded-Port 443
ProxyPreserveHost On
<If "%{HTTP_HOST} =~ /www\./">
    RedirectMatch (.*) https://<DOMAIN_NAME_HERE>$1
</If>

sudo service apache2 start
```



systemctl --type=service
systemctl status apache2.service
cd /etc/systemd/system
sudo chmod 777 simple-app.service
sudo systemctl enable simple-app.service
sudo systemctl enable simple-app.service

### Confirm Name Servers Match

https://console.aws.amazon.com/route53/home


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
            "Resource": "arn:aws:s3:::content.chhcsfun.com/*"
        },
        {
            "Sid": "OneAndHalf",
            "Effect": "Allow",
            "Action": [
                "s3:ListBucket",
                "s3:GetBucketPolicy",
                "s3:PutBucketPolicy"
            ],
            "Resource": "arn:aws:s3:::content.chhcsfun.com"
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
            "Resource": "arn:aws:dynamodb:us-east-1:<AWS_ACCOUNT_ID>:table/userAppData.chhcsfun.com"
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


Create User and Entities

aws s3 sync --acl public-read --exclude ".*" ./content s3://content.chhcsfun.com/content


## Preparations for Setup

### Install the `aws` Command Line Client

### Prepare to Write Down Variable Values Specific to the Application

There are several values listed in these instructions which will only be known by you.
These values cannot be determined ahead of time. As these values come up, record them
in a common table that you can reference later.

#### Representation of Variable Values

When these values come up for you to enter, they will appear as their variable name
and surrounded by the `#` character. So if the variable name `DOMAIN_NAME` has a value
of `chhcsfun.com`, then when you're asked to enter a statement like the following.

```
export TF_VAR_DOMAIN_NAME="#DOMAIN_NAME#"
```

Enter the following instead.

```
export TF_VAR_DOMAIN_NAME="chhcsfun.com"
```

#### List of Variable Values

Write down (either electronically or on paper) a table consisting of two columns.
The left column should be filled with the variable names listed below and in the right
column write the value of that variable as you discover it.

  - `DOMAIN_NAME` - The hostname (like 'chhcsfun.com') of the web site

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

###

// What Is This?
// Create 'INSTALLATION.md' File

Don't need to understand entire application, don't get intimidated
Technologies change about every 7 years
Could just throw files into the 'resources/static' directory
  //
Access permissions
Instructions
Alerts
Update service to reboot
Calling web services from Phaser
Delete everything in the AWS account
