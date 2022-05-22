
# Ignore This Section




#### Create a User and Configure the `aws` Tool to Perform Actions as That User

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


./mvnw clean package spring-boot:repackage

Create SSL certificate for ELB in AWS Certificate Manager
  - Click on the 'Pending validation' option and select 'Create record in Route 53'

## Install and Configure the Server


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

Create User and Entities

aws s3 sync --acl public-read --exclude ".*" ./content s3://content.chhcsfun.com/content


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

//

Verification Instructions, Delete Instructions, Backend State Store
Server-Side Example, Phaser Example, Prevent Pool Delete
Route53 Instructions, API Usage Instructions (with Login)
Non-Data Hosted Zone, Cross-Account User Visibility
Troubleshooting (Reboot, Checking Logs)

sudo nano /etc/apt/apt.conf.d/20auto-upgrades
https://linuxhint.com/enable-disable-unattended-upgrades-ubuntu/
sudo dpkg-reconfigure unattended-upgrades
  - Select 'No' option
