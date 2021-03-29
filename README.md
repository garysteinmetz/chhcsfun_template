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

Generate user and get key-secret pair
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
