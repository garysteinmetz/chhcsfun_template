# Services and Tools

[Click here to go back to the main page.](../../README.md)

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

[Click here to go back to the main page.](../../README.md)
