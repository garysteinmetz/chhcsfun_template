# chhcsfun_template

## Goals

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

## Incremental Development

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

#### Delete Route53 Domain

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