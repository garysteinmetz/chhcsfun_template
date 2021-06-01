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

## Route Apache Traffic to the Application and Support the HTTPS Protocol

`Apache` is a web server which is specialized for handling public calls. It's very good
at doing things like routing traffic and supporting the HTTPS protocol.

The goal here is to configure `Apache` to support HTTPS and act as a conduit
to the application.

### Initiate Terminal Session with LightSail Instance

While it is possible to initiate a terminal session with the 'ssh' protocol
from the command prompt, the AWS console has a browser-based version of an 'ssh' session
which is convenient to use.

Create this session (in a browser window) with the following steps.

  - Open a browser window and go to `#TF_VAR_AWS_DEVOPS_CONSOLE_URL#`
  - Login as the `DevOps User` using `#TF_VAR_AWS_DEVOPS_USERNAME#` as the username
  and `#TF_VAR_AWS_DEVOPS_PASSWORD#` as the password
  - In the search box, enter 'Lightsail' and select the 'Lightsail' result
  - The 'Instances' tab should be selected on the page, select it if it isn't
  - In the larger instance box labeled `#TF_VAR_AWS_DOMAIN_NAME#`,
  click the three-verticle-dots icon and select 'Connect' which should open
  a pop-up window displaying a command prompt of the LightSail instance
    - Note - This window times out very quickly (even after a few minutes idle),
    if you get logged out just re-follow the steps above to open another
    LightSail command prompt

### Confirm That Both the Apache Server and the Application Are Responsive

In the browser terminal window, run the following commands.

  - `curl http://localhost`
    - This should output the same contents as `http://#TF_VAR_AWS_DOMAIN_NAME#`
    - `curl --silent http://localhost | grep title`
      - This should output `<title>Apache2 Ubuntu Default Page: It works</title>`
  - `curl http://localhost:8080`
    - If you are still running the application locally,
    this should output the same contents as `http://localhost:8080`

This shows that port 80 (the default port of HTTP) is visible to the outside world.
Apache uses this port while the application uses port 8080 which isn't visible
to the outside world, _But_ Apache can be configured to forward traffic to that port.

### Configure Apache to Use HTTPS

In the browser terminal window, run the following commands to enable HTTPS
on the `Apache` server and forward requests from the 80 (HTTP) port
to the 443 (HTTPS) port.

  - `sudo snap install core`
  - `sudo snap refresh core`
  - `sudo apt-get remove certbot`
  - `sudo snap install --classic certbot`
  - `sudo ln -s /snap/bin/certbot /usr/bin/certbot`
  - `sudo certbot --apache`
    - You will be prompted to enter values
      - Enter your email address
      - Enter 'Y' to agree with the 'Terms of Service'
      - Determine whether you'd like to receive email from the EFF org (enter 'Y' or 'N')
      - For the 'Please enter the domain name(s) ...' prompt, enter
      `#TF_VAR_AWS_DOMAIN_NAME#,www.#TF_VAR_AWS_DOMAIN_NAME#`
      - Enter '2' to select the '000-default-le-ssl.conf' virtual host for the 'www.' subdomain

Wait a few minutes then try to access the 'https' version of the URL (including 'www.').

  - In a browser, go to `http://#TF_VAR_AWS_DOMAIN_NAME#` and note that the request
  is forwarded to `https://#TF_VAR_AWS_DOMAIN_NAME#`
  - In a browser, go to `http://www.#TF_VAR_AWS_DOMAIN_NAME#` and note that the request
  is forwarded to `https://www.#TF_VAR_AWS_DOMAIN_NAME#`

### Forward Traffic to the Application, Redirect 'www.' Calls

In the browser terminal window, run the following commands to forward traffic
(browser calls) to the application, and redirect calls
from `https://www.#TF_VAR_AWS_DOMAIN_NAME#` to `https://#TF_VAR_AWS_DOMAIN_NAME#` .

  - `sudo a2enmod proxy_http`
  - `sudo a2enmod headers`
  - `sudo service apache2 stop`

Now edit the `Apache` HTTPS configuration file to forward traffic to the application
(on port 8080) and redirect 'www.' requests. Open the file for editing
with the following command.

  - `sudo vi /etc/apache2/sites-available/000-default-le-ssl.conf`

Now put the following just above the '</VirtualHost>' end tag near the bottom of the file.
Make sure to substitute the `#TF_VAR_AWS_DOMAIN_NAME#` value with the correct domain
name of your web site.

```
ProxyPass / http://127.0.0.1:8080/
RequestHeader set X-Forwarded-Proto https
RequestHeader set X-Forwarded-Port 443
ProxyPreserveHost On
<If "%{HTTP_HOST} =~ /www\./">
    RedirectMatch (.*) https://#TF_VAR_AWS_DOMAIN_NAME#$1
</If>
```

Finally restart the `Apache` server.

  - `sudo service apache2 start`

### Confirm Work

Open a browser and go to `http://www.#TF_VAR_AWS_DOMAIN_NAME#` and confirm
that the browser (A) gets forwarded to `https://#TF_VAR_AWS_DOMAIN_NAME#`
and (B) now renders the ('coin flip') application.

[Click here to go back to the main page.](../../README.md)
