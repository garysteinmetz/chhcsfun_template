data aws_caller_identity current {}

locals {
  # Note - S3 bucket names must be unique througout AWS so append user ID to ensure uniqueness
  bucket_name = "chhcs.${data.aws_caller_identity.current.user_id}"
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
  user_data = <<EOF
sudo yum -y install java-1.8.0-openjdk-devel.x86_64
sudo update-alternatives --set java /usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin/java
EOF
  depends_on = [aws_s3_bucket.chhcsfun]
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

resource aws_iam_policy_attachment chhcsfun_policy_attachment {
  name = "chhcsfun_policy_attachment"
  users = [aws_iam_user.chhcsfun_lightsail_user.name]
  policy_arn = aws_iam_policy.policy.arn
}

resource aws_s3_bucket chhcsfun {
  bucket = local.bucket_name
  acl = "private"
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
