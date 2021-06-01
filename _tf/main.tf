data aws_caller_identity current {}
data aws_region current {}

provider aws {
  region = var.AWS_REGION
}

locals {
  lightsail_availability_zone = length(var.AWS_AVAILABILITY_ZONE) > 0 ? var.AWS_AVAILABILITY_ZONE : "${var.AWS_REGION}a"
  # Note - S3 bucket names must be unique througout AWS so append user ID to ensure uniqueness
  lightsail_instance_name = var.AWS_DOMAIN_NAME
  lightsail_startup_script = <<EOF
sudo su ${var.APP_OS_USER}
cd $(getent passwd ${var.APP_OS_USER} | cut -d: -f6)
sudo apt update
sudo apt-get -y install openjdk-11-jdk-headless
sudo apt-get -y install awscli
sudo aws configure set aws_access_key_id "${aws_iam_access_key.chhcsfun_lightsail_user.id}"
sudo aws configure set aws_secret_access_key "${aws_iam_access_key.chhcsfun_lightsail_user.secret}"
sudo aws configure set default.region "${data.aws_region.current.name}"
sudo aws lightsail put-instance-public-ports --instance-name=${local.lightsail_instance_name} --port-infos ${var.APP_PORTS}
sudo aws s3 cp s3://${var.AWS_S3_BUCKET_NAME_CONTENT}/app.jar ./app.jar 1> ./resultDownloadApp.txt 2> ./errorDownloadApp.txt
touch ./initEnv.sh
echo "export 'TF_VAR_AWS_S3_BUCKET_NAME_CONTENT=${var.AWS_S3_BUCKET_NAME_CONTENT}'" >> ./initEnv.sh
echo "export 'TF_VAR_AWS_DYNAMODB_TABLE_NAME_USERAPPDATA=${var.AWS_DYNAMODB_TABLE_NAME_USERAPPDATA}'" >> ./initEnv.sh
echo "export 'TF_VAR_AWS_COGNITO_CLIENT_ID=${var.AWS_COGNITO_CLIENT_ID}'" >> ./initEnv.sh
echo "export 'TF_VAR_AWS_COGNITO_CLIENT_SECRET=${var.AWS_COGNITO_CLIENT_SECRET}'" >> ./initEnv.sh
echo "export 'TF_VAR_AWS_COGNITO_DOMAIN_NAME=${var.AWS_COGNITO_DOMAIN_NAME}'" >> ./initEnv.sh
echo "export 'TF_VAR_AWS_COGNITO_OAUTH2_AUTHORIZE_URL=${var.AWS_COGNITO_OAUTH2_AUTHORIZE_URL}'" >> ./initEnv.sh
echo "export 'TF_VAR_AWS_COGNITO_OAUTH2_TOKEN_URL=${var.AWS_COGNITO_OAUTH2_TOKEN_URL}'" >> ./initEnv.sh
echo "export 'TF_VAR_AWS_COGNITO_USER_POOL_ID=${var.AWS_COGNITO_USER_POOL_ID}'" >> ./initEnv.sh
chmod 777 ./initEnv.sh
cat ./initEnv.sh > pre.out
. ./initEnv.sh > source.out
export >> env.out
echo "source ./initEnv.sh" >> ./.bashrc
sudo apt update > ./apt.update.out
sudo apt -y install apache2 > ./apt.install.apache2.out
touch /home/ubuntu/app.sh
echo "#!/bin/bash" >> /home/ubuntu/app.sh
echo "source /home/ubuntu/initEnv.sh" >> /home/ubuntu/app.sh
echo "java -jar /home/ubuntu/app.jar" >> /home/ubuntu/app.sh
chmod 777 /home/ubuntu/app.sh
touch /etc/systemd/system/simple-app.service
echo "[Unit]" >> /etc/systemd/system/simple-app.service
echo "Description=Simple Application" >> /etc/systemd/system/simple-app.service
echo "StartLimitIntervalSec=0" >> /etc/systemd/system/simple-app.service
echo "" >> /etc/systemd/system/simple-app.service
echo "[Service]" >> /etc/systemd/system/simple-app.service
echo "ExecStart=/home/ubuntu/app.sh" >> /etc/systemd/system/simple-app.service
echo "Restart=always" >> /etc/systemd/system/simple-app.service
echo "RestartSec=1" >> /etc/systemd/system/simple-app.service
echo "" >> /etc/systemd/system/simple-app.service
echo "[Install]" >> /etc/systemd/system/simple-app.service
echo "WantedBy=multi-user.target" >> /etc/systemd/system/simple-app.service
chmod 777 /etc/systemd/system/simple-app.service
sudo systemctl enable simple-app.service
sudo systemctl start simple-app
EOF
}

#https://docs.aws.amazon.com/Route53/latest/DeveloperGuide/troubleshooting-new-dns-settings-not-in-effect.html#troubleshooting-new-dns-settings-not-in-effect-updated-wrong-hosted-zone
#  Section - "You have more than one hosted zone with the same name, and you updated the one that isn't associated with the domain"
#https://registrar.amazon.com/whois
#https://www.iana.org/domains/root/servers
#https://certbot.eff.org/lets-encrypt/ubuntufocal-apache
#sudo vi /etc/apache2/sites-available/000-default-le-ssl.conf
#https://medium.com/@codebyamir/using-apache-as-a-reverse-proxy-for-spring-boot-embedded-tomcat-f704da73e7c8
#sudo a2enmod proxy_http
#sudo a2enmod headers
#sudo service apache2 start
#ProxyPass / http://127.0.0.1:8080/
#RequestHeader set X-Forwarded-Proto https
#RequestHeader set X-Forwarded-Port 443
#ProxyPreserveHost On
#https://httpd.apache.org/docs/2.4/rewrite/remapping.html
#<If "%{HTTP_HOST} =~ '^www\.(.*)$'">
#    RedirectMatch (.*) <DOMAIN_NAME_HERE>$1
#</If>
#<If "%{HTTP_HOST} =~ /www\./">
#    RedirectMatch (.*) <DOMAIN_NAME_HERE>$1
#</If>
#https://httpd.apache.org/docs/current/expr.html
#<If "%{HTTP_HOST} =~ /www\./">
#    RedirectMatch (.*) https://${SERVER_NAME}$1
#</If>
#https://stackoverflow.com/questions/4834141/apache-php-any-way-to-retrieve-the-servername-setting-via-php
#https://stackoverflow.com/questions/48972967/apache-server-name-not-resolving-to-servername
#https://cwiki.apache.org/confluence/display/HTTPD/FAQ#Why_does_Apache_ask_for_my_password_twice_before_serving_a_file.3F


# Note - Use eTag
data aws_s3_bucket chhcsfun {
  bucket = var.AWS_S3_BUCKET_NAME_CONTENT
}

data aws_dynamodb_table chhcsfun {
  name = var.AWS_DYNAMODB_TABLE_NAME_USERAPPDATA
}

data aws_route53_zone primary {
  name = var.AWS_DOMAIN_NAME
}
resource aws_route53_record main {
  zone_id = data.aws_route53_zone.primary.zone_id
  name = var.AWS_DOMAIN_NAME
  type = "A"
  ttl = 300
  records = [aws_lightsail_static_ip_attachment.chhcsfun.ip_address]
}
resource aws_route53_record www {
  zone_id = data.aws_route53_zone.primary.zone_id
  name = "www"
  type = "CNAME"
  ttl = 300
  records = [aws_route53_record.main.name]
}

resource aws_lightsail_static_ip chhcsfun {
  name = "${var.AWS_DOMAIN_NAME}_static_ip"
}

resource aws_lightsail_static_ip_attachment chhcsfun {
  static_ip_name = aws_lightsail_static_ip.chhcsfun.id
  instance_name = aws_lightsail_instance.chhcsfun.id
}

resource aws_iam_access_key chhcsfun_lightsail_user {
  user    = aws_iam_user.chhcsfun_lightsail_user.name
}

resource aws_iam_user chhcsfun_lightsail_user {
  name = "${var.AWS_DOMAIN_NAME}_lightsail_user"
}

resource aws_lightsail_instance chhcsfun {
  name = local.lightsail_instance_name
  availability_zone = local.lightsail_availability_zone
  blueprint_id = "ubuntu_20_04"
  bundle_id = "nano_2_0"
  user_data = local.lightsail_startup_script
  depends_on = [data.aws_s3_bucket.chhcsfun, aws_s3_bucket_object.chhcsfun_app_file]
}

resource aws_iam_policy policy {
  name = "app_policies"
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": ["s3:GetObject"],
      "Effect": "Allow",
      "Resource": "arn:aws:s3:::${var.AWS_S3_BUCKET_NAME_CONTENT}/*"
    },
    {
      "Action": ["dynamodb:GetItem", "dynamodb:PutItem"],
      "Effect": "Allow",
      "Resource": "${data.aws_dynamodb_table.chhcsfun.arn}"
    },
    {
      "Action": ["lightsail:PutInstancePublicPorts"],
      "Effect": "Allow",
      "Resource": "arn:aws:lightsail:${data.aws_region.current.name}:${data.aws_caller_identity.current.account_id}:Instance/*"
    }
  ]
}
EOF
}
#      "Resource": ""${aws_s3_bucket.chhcsfun.arn}/*"
#      "Resource": "arn:aws:s3:::${local.bucket_name}/*"
#sudo aws s3 cp s3://${local.bucket_name}/sampleOne.txt ./sampleOne.txt
#  - 'The operating system seems to be based on CentOS 7.'
#https://www.theregister.com/2020/04/22/aws_introduces_linux_2_ready/
#https://docs.aws.amazon.com/cognito/latest/developerguide/cognito-user-pools-app-idp-settings.html
#  - "Amazon Cognito requires HTTPS over HTTP except for http://localhost for testing purposes only."

#https://certbot.eff.org/lets-encrypt/centosrhel7-nginx

#https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/SSL-on-amazon-linux-2.html

resource aws_iam_policy_attachment chhcsfun_policy_attachment {
  name = "chhcsfun_policy_attachment"
  users = [aws_iam_user.chhcsfun_lightsail_user.name]
  policy_arn = aws_iam_policy.policy.arn
}

resource aws_s3_bucket_object chhcsfun_example_file {
  key = "sampleOne.txt"
  bucket = data.aws_s3_bucket.chhcsfun.id
  content = "Here is one sample"
}

resource aws_s3_bucket_object chhcsfun_app_file {
  key = "app.jar"
  bucket = data.aws_s3_bucket.chhcsfun.id
  source = "../target/demo-0.0.1-SNAPSHOT.jar"
  etag = filemd5("../target/demo-0.0.1-SNAPSHOT.jar")
}

output id {
  value = aws_iam_access_key.chhcsfun_lightsail_user.id
}

output secret {
  value = aws_iam_access_key.chhcsfun_lightsail_user.secret
}

output account_id {
  value = data.aws_caller_identity.current.account_id
}

output caller_arn {
  value = data.aws_caller_identity.current.arn
}

output caller_user {
  value = data.aws_caller_identity.current.user_id
}

output caller_region {
  value = data.aws_region.current.name
}

output lightsail_startup_script {
  value = local.lightsail_startup_script
}
