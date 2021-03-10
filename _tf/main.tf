data aws_caller_identity current {}
data aws_region current {}

locals {
  # Note - S3 bucket names must be unique througout AWS so append user ID to ensure uniqueness
  bucket_name = "chhcs.${data.aws_caller_identity.current.user_id}"
  lightsail_startup_script = <<EOF
sudo yum -y install java-1.8.0-openjdk-devel.x86_64
sudo update-alternatives --set java /usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin/java
sudo aws configure set aws_access_key_id "${aws_iam_access_key.chhcsfun_lightsail_user.id}"
sudo aws configure set aws_secret_access_key "${aws_iam_access_key.chhcsfun_lightsail_user.secret}"
sudo aws configure set default.region "${data.aws_region.current.name}"
sudo aws s3 cp s3://${local.bucket_name}/app.jar /app.jar 1> /resultUploadingApp.txt 2> /errorUploadingApp.txt
EOF
}

resource aws_iam_access_key chhcsfun_lightsail_user {
  user    = aws_iam_user.chhcsfun_lightsail_user.name
}

resource aws_iam_user chhcsfun_lightsail_user {
  name = "chhcsfun_lightsail_user"
}

resource aws_lightsail_instance chhcsfun {
  name = "chhcsfun"
  availability_zone = "us-east-1a"
  blueprint_id = "amazon_linux"
  bundle_id = "nano_2_0"
  user_data = local.lightsail_startup_script
  depends_on = [aws_s3_bucket.chhcsfun, aws_s3_bucket_object.chhcsfun_app_file]
}

resource aws_iam_policy policy {
  name = "chhcsfun_policies"
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": ["s3:GetObject"],
      "Effect": "Allow",
      "Resource": "arn:aws:s3:::${local.bucket_name}/*"
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

#https://certbot.eff.org/lets-encrypt/centosrhel7-nginx

#https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/SSL-on-amazon-linux-2.html

resource aws_iam_policy_attachment chhcsfun_policy_attachment {
  name = "chhcsfun_policy_attachment"
  users = [aws_iam_user.chhcsfun_lightsail_user.name]
  policy_arn = aws_iam_policy.policy.arn
}

# Note - Use eTag
resource aws_s3_bucket chhcsfun {
  bucket = local.bucket_name
  acl = "private"
}

resource aws_s3_bucket_object chhcsfun_example_file {
  key = "sampleOne.txt"
  bucket = aws_s3_bucket.chhcsfun.id
  content = "Here is one sample"
}

resource aws_s3_bucket_object chhcsfun_app_file {
  key = "app.jar"
  bucket = aws_s3_bucket.chhcsfun.id
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

output lightsail_startup_script {
  value = local.lightsail_startup_script
}
