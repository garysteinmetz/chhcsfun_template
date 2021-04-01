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
