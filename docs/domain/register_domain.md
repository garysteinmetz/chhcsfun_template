# Register an Unused Domain Name

[Click here to go back to the main page.](../../README.md)

Think of the domain name (like 'chhcsfun.com') for your web site. You don't need to actually register
(and pay for) a domain name right now. You will only need to register a domain name once you've
decided to host your web application on a LightSail server. _A domain name isn't needed to run
the web application locally but the AWS entities (like the DynamoDB table and Cognito entities)
will be assigned names that contain the domain name so correctly identifying the desired domain name
now will allow for migration of the local web application into the cloud._

Note that your selection of a domain name here (obviously) doesn't mean that you have formally
registered it in AWS. You only register (temporarily own on a yearly basis) a domain and are charged
by AWS for that registration by following these steps (which can also be used to determine if a domain
name is available without actually reserving it).

  - Logged in as `Root User` , go to https://console.aws.amazon.com/route53/home
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

Record this domain name as the `TF_VAR_AWS_DOMAIN_NAME` variable value.
_(Don't include 'www.' in it!)_

[Click here to go back to the main page.](../../README.md)
