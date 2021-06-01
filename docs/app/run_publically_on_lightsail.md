# Run This Application Publicly

[Click here to go back to the main page.](../../README.md)

## Create Route 53 Zone Aligned with _#TF_VAR_AWS_DOMAIN_NAME#_

As the `Root User`, do the following.

  - Go to https://aws.amazon.com and login as you would on normal Amazon (as `Root User`)
    - The browser should go to https://console.aws.amazon.com/console/home
  - Make sure you have selected the correct AWS region (like 'US East (N. Virginia) us-east-1')
  - In the search box, enter 'Hosted zones' and select the 'Hosted zones' result
  - _If `#TF_VAR_AWS_DOMAIN_NAME#` appears under the 'Domain name' column, skip
  the rest of the steps in the steps here_
  - Click the 'Create hosted zone' button
  - In the 'Domain name' field, enter `#TF_VAR_AWS_DOMAIN_NAME#`
  - Under 'Type', make sure the 'Public hosted zone' radio button is clicked
  - Click the 'Create hosted zone' button

### Confirm that the `NS` Records in the Route 53 Zone Align with Registered Domain

_This section is important. Even if you didn't create a zone in the steps above,
still follow the steps in this section._

#### Determine the `NS` Entries Assigned to the Registered Domain

  - Go to https://lookup.icann.org/lookup and enter `#TF_VAR_AWS_DOMAIN_NAME#`
  in the 'Enter a domain name' box then click the 'Lookup' button
  - Copy the results (there are typically 4 of them) under the 'Nameservers'
  results under the 'Domain Information' section (these results will have
  values like 'NS-1066.AWSDNS-05.ORG')
  - Go to https://aws.amazon.com and login as you would on normal Amazon (as `Root User`)
  - Make sure you have selected the correct AWS region (like 'US East (N. Virginia) us-east-1')
  - In the search box, enter 'Hosted zones' and select the 'Hosted zones' result
  - Click `#TF_VAR_AWS_DOMAIN_NAME#` under the 'Domain name' column
  - In the table, click the checkbox next to the entry of 'Type' value 'NS'
  - Check the (likely 4) values under the 'Values' section
    - If these values are the same as the 'Nameservers' listed above,
    then no changes are necessary and you should skip the rest of this section
    - Note - When comparing values, ignore the '.' that appears at the end of each
    entry and don't worry if the letters are of different case
    (so 'NS-1066.AWSDNS-05.ORG' and 'ns-1066.awsdns-05.org.' correspond to the same value)
  - If any value in this section doesn't have a corresponding value in the 'Nameservers'
  listed above, override all the values by doing the following
    - Click the 'Edit record' button
    - Under the 'Value' section, delete all of the contents in the text box, then paste in
    the values from the 'Nameservers' listed above
      - Don't worry about letter case or if a '.' doesn't appear at the end of an entry
    - Click the 'Save' button

## Use Terraform to Create LightSail Instance with Apache and the Application Running on It

_Now it's time to integrate all the AWS configurations to create a public web site!_

  - Open a command prompt and go to the '_tf' (for Terraform) subdirectory from this directory
    - Ensure that the variable values you've recorded previously are set up as environment
    variables in this command prompt
      - On Mac, that can be done by running command `source ~/Desktop/initVars.sh`
  - Run the following command to initialize Terraform for use in the current directory
    - `terraform init`
  - Create the LightSail instance with Apache and the application by running this command
  and entering 'yes' when prompted
    - `terraform apply`
  - Wait a few minutes as the script completes
  - Open a browser and go to `http://#TF_VAR_AWS_DOMAIN_NAME#` and confirm that a web page
  entitled 'Apache2 Ubuntu Default Page' appears
    - Note that 'http' is used here, not 'https' (that will be added later)

[Click here to go back to the main page.](../../README.md)
