# chhcsfun_template

## Introduction

Follow the instructions below. Afterwards, use the [maintenance page ](MAINTENANCE.md)
as needed afterwards.

## Goals

The goal of this project is to give you the ability to host an application server in AWS
with support for HTTPS, user login, and per-user data storage.

This application server can also be run on your local machine with the option of either
simulating ('mocking') interaction with AWS or actually integrating with AWS.

AWS has a vast portfolio of categories of services. Everything from renting computer time,
storing data, identifying faces in pictures, crowdsourcing the talents of other people
('Mechnical Turk'), and so much more can be done with AWS.

[Click here to find out more information about the services and tools
that will be used.](docs/goals/services_and_tools.md)

## Run This Application Locally Without AWS Integration

Confirm that you can run this application locally (without integration to AWS)
by following the steps [here](docs/sample_app/coin_flip.md).  This is the 'coin flip' game
and environment variables will be set later in these instructions so that this game
actually integrates with AWS.

## User Types

While it may be just one actual person (you) that installs this application,
these instructions require two AWS users to complete.

  - `Root User` - This is the user whose login (username/password) you use to order
  things (like books) on regular `amazon.com` . This user can also login and do _anything_
  on AWS (`aws.amazon.com`). Because this user has _too much power_ and it's quite possible
  that the person paying the AWS costs to use this application (e.g. parent) is different
  from the person installing and maintaining this application (e.g. child), it's best
  that this user create another user who is has just enough rights (and no more)
  to install and maintain this application.
  - `DevOps User` - This is the user who can't login to regular `amazon.com` but has just
  enough privileges to install and maintain this application.

The owner of the account (the one who pays the bills - `Root User`) should _not_ be the one
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


In these instructions, the `Root User` will be creating the `DevOps User` along with the AWS
structures (like Cognito User Group, DynamoDB table, Route53 zone, S3 bucket). The `DevOps User`
will be using these AWS structures and create the LightSail instance to run this application.
The `DevOps User` will also have the ability to destroy and to recreate the LightSail instance.

## Billing

This setup involves using AWS resources, the usage of some of which
(e.g. Route53, LightSail) will incur charges billed monthly.

### Viewing Billing

To view the accumulated charges for this month go to
https://console.aws.amazon.com/billing/home#/ . On that page, click the 'Bills' link
to view the bills for previous months.

[Click here to find out how to create budget alerts to help detect
unexpected charges.](docs/billing/create_budgets.md)

## Variable Values

### Get Ready to Record Variable Values

There are several values listed in these instructions which will only be known by you.
These values cannot be determined ahead of time. As these values come up, record them
in a common table that you can reference later.

### Representation of Variable Values

When these values come up for you to enter, they will appear as their variable name
and surrounded by the `#` character. So if the variable name `TF_VAR_AWS_DOMAIN_NAME`
has a value of `chhcsfun.com`, then when you're asked to enter a statement
like the following.

```
export TF_VAR_DOMAIN_NAME="#TF_VAR_AWS_DOMAIN_NAME#"
```

Enter the following instead.

```
export TF_VAR_DOMAIN_NAME="chhcsfun.com"
```

### List of Variable Values

Write down (either (preferably) electronically or on paper) a table consisting of two columns.
The left column should be filled with the variable names listed below and in the right
column write the value of that variable as you discover it. _Note that some of these values
should be kept secret so don't share this list with others._

  - `TF_VAR_AWS_ACCOUNT_ID` - This is the unique number assigned to your AWS account
  - `TF_VAR_AWS_REGION` - This should have a value of `us-east-1`
  - `TF_VAR_AWS_DOMAIN_NAME` - The hostname (like 'chhcsfun.com') of the web site
  - `TF_VAR_AWS_S3_BUCKET_NAME_CONTENT` - The name of the S3 bucket
  - `TF_VAR_AWS_DYNAMODB_TABLE_NAME_USERAPPDATA` - The name of the DynamoDB table
  - `TF_VAR_AWS_COGNITO_USER_POOL_ID` - The AWS ID assigned to the pool of users of your application
  - `TF_VAR_AWS_COGNITO_CLIENT_ID` - The client ID (like a username) to access Cognito
  - `TF_VAR_AWS_COGNITO_CLIENT_SECRET` - The client secret (like a password) to access Cognito
  - `TF_VAR_AWS_COGNITO_DOMAIN_NAME` - The base URL for your application's login capabilities
  - `TF_VAR_AWS_COGNITO_OAUTH2_AUTHORIZE_URL` - The login URL for users using your application
  - `TF_VAR_AWS_COGNITO_OAUTH2_TOKEN_URL` - The URL your application accesses to confirm a user login
  - `TF_VAR_AWS_DEVOPS_CONSOLE_URL` - The login page of the `DevOps User`
  - `TF_VAR_AWS_DEVOPS_USERNAME` - The username of the specialized account used to access AWS
  - `TF_VAR_AWS_DEVOPS_PASSWORD` - The password of the specialized account used to access AWS
  - `TF_VAR_AWS_DEVOPS_ACCESS_KEY_ID` - This is the equivalent of a username for `aws` tool
  usage for the specialized account
  - `TF_VAR_AWS_DEVOPS_SECRET_ACCESS_KEY` - This is the equivalent of a password for `aws` tool
  usage for the specialized account

#### Convenience Script for MacOS

On your desktop, create a text file named `initVars.sh` which can be used to first
record these variable values. Later, they'll be used to easily initialize the local setup
of the application and then deploy it to AWS. Enter the following content into the file.

```
export TF_VAR_AWS_ACCOUNT_ID=""
export TF_VAR_AWS_REGION=""
export TF_VAR_AWS_DOMAIN_NAME=""
export TF_VAR_AWS_S3_BUCKET_NAME_CONTENT=""
export TF_VAR_AWS_DYNAMODB_TABLE_NAME_USERAPPDATA=""
export TF_VAR_AWS_COGNITO_USER_POOL_ID=""
export TF_VAR_AWS_COGNITO_CLIENT_ID=""
export TF_VAR_AWS_COGNITO_CLIENT_SECRET=""
export TF_VAR_AWS_COGNITO_DOMAIN_NAME=""
export TF_VAR_AWS_COGNITO_OAUTH2_AUTHORIZE_URL=""
export TF_VAR_AWS_COGNITO_OAUTH2_TOKEN_URL=""
export TF_VAR_AWS_DEVOPS_CONSOLE_URL=""
export TF_VAR_AWS_DEVOPS_USERNAME=""
export TF_VAR_AWS_DEVOPS_PASSWORD=""
export TF_VAR_AWS_DEVOPS_ACCESS_KEY_ID=""
export TF_VAR_AWS_DEVOPS_SECRET_ACCESS_KEY=""
```

As you derive these values following the rest of the steps in these instructions,
enter each one in its corresponding line below between the `"` pair. So for the
`export TF_VAR_AWS_DOMAIN_NAME=""` line below, if 'chhcsfun.com' is used as the value
then the line should be updated to `export TF_VAR_AWS_DOMAIN_NAME="chhcsfun.com"` .

Make sure you can execute this script to load these (environment) variable values
on the command line by running command `chmod 777 ~/Desktop/initVars.sh` .

Whenever you need to load these (environment) variable values from the command line,
run command `source ~/Desktop/initVars.sh` .

#### Convenience Script for Windows



## For `Root User`, Record the AWS Account ID and Set the Default Region

  - Go to https://aws.amazon.com and login as the `Root User`
  - Click your name in the top right and record the number next to 'My Account'
  as the `TF_VAR_AWS_ACCOUNT_ID` variable value

### Always Use the `us-east-1` AWS Region

AWS hosts its services all around the world in well-defined regions. It's important
to use the same region when you're creating the AWS components for an application.
(Note that some components, like S3 'buckets' and IAM users are 'global' in that they
aren't assigned to specific regions. They are region-less.)

This setup will assume that you are using the `us-east-1` region, but you can select
another region. _Make sure to use the same region throughout this installation._
Ensure this setting by doing the following.

  - Go to any AWS page like https://console.aws.amazon.com/console/home
  - Just right of your name in the top right, click the dropdown and select the
  `US East (N. Virginia) us-east-1` value
  - Record this region's code (`us-east-1`) as the `TF_VAR_AWS_REGION` variable value

### Confirm Variable Values

Confirm that the following variable values have been recorded in this section.

  - `TF_VAR_AWS_ACCOUNT_ID`
  - `TF_VAR_AWS_REGION`

## Register an Unused Domain Name

[Click here to find out how to register a domain name.](docs/domain/register_domain.md)

### Confirm Variable Values

Confirm that the following variable values have been recorded in this section.

  - `TF_VAR_AWS_DOMAIN_NAME`

## Create AWS Entity - Create S3 Bucket

[Click here to find out how to create an S3 bucket.](docs/aws/create_s3_bucket.md)

### Confirm Variable Values

Confirm that the following variable values have been recorded in this section.

  - `TF_VAR_AWS_S3_BUCKET_NAME_CONTENT`

## Create AWS Entity - Create DynamoDB Table

[Click here to find out how to create a DynamoDB table.](docs/aws/create_dynamodb_table.md)

### Confirm Variable Values

Confirm that the following variable values have been recorded in this section.

  - `TF_VAR_AWS_DYNAMODB_TABLE_NAME_USERAPPDATA`

## Create AWS Entity - Create Cognito User Pool

[Click here to find out how to create a Cognito user pool.](docs/aws/create_cognito_user_pool.md)

### Confirm Variable Values

Confirm that the following variable values have been recorded in this section.

  - `TF_VAR_AWS_COGNITO_USER_POOL_ID`
  - `TF_VAR_AWS_COGNITO_CLIENT_ID`
  - `TF_VAR_AWS_COGNITO_CLIENT_SECRET`
  - `TF_VAR_AWS_COGNITO_DOMAIN_NAME`
  - `TF_VAR_AWS_COGNITO_OAUTH2_AUTHORIZE_URL`
  - `TF_VAR_AWS_COGNITO_OAUTH2_TOKEN_URL`

## Create AWS Entity - `DevOps` User

[Click here to find out how to create the `DevOps` user.](docs/aws/create_devops_iam_user.md)

### Confirm Variable Values

Confirm that the following variable values have been recorded in this section.

  - `TF_VAR_AWS_DEVOPS_CONSOLE_URL`
  - `TF_VAR_AWS_DEVOPS_USERNAME`
  - `TF_VAR_AWS_DEVOPS_PASSWORD`
  - `TF_VAR_AWS_DEVOPS_ACCESS_KEY_ID`
  - `TF_VAR_AWS_DEVOPS_SECRET_ACCESS_KEY`


## Run This Application Locally _With_ AWS Integration

[Click here to find out how to run the application
locally with AWS integration.](docs/app/run_locally_with_aws.md)

### Confirm Local Integration with AWS

As documented in the link above, the game's files should be uploaded to S3,
the user is listed in the Cognito user group, and that user's data is listed
in the DynamoDB table.

## Run This Application Publicly

[The moment has arrived - click here to run this application on the internet
 for all to use!](docs/app/run_publically_on_lightsail.md)

### Confirm Application Is Publicly Available

Open a browser and go to `https://#TF_VAR_AWS_DOMAIN_NAME#` and play the 'coin flip' game!
Ask your family and friends to play it too.

Optionally, as the `DevOps User` confirm that all users have entries
in the Cognito user group and the DynamoDB table.
