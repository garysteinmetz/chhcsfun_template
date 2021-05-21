



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




##### Test the `aws` Command-Line Tool

### Pre-Installation Setup

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
