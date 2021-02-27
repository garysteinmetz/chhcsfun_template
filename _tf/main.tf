data "aws_caller_identity" "current" {}

resource "aws_iam_access_key" "chhcsfun_lightsail_user" {
  user    = aws_iam_user.chhcsfun_lightsail_user.name
}

resource "aws_iam_user" "chhcsfun_lightsail_user" {
  name = "chhcsfun_lightsail_user"
}

resource aws_lightsail_instance chhcsfun {
  name = "chhcsfun"
  availability_zone = "us-east-1a"
  blueprint_id = "amazon_linux"
  bundle_id = "nano_2_0"
}

output "id" {
  value = aws_iam_access_key.chhcsfun_lightsail_user.id
}

output "secret" {
  value = aws_iam_access_key.chhcsfun_lightsail_user.secret
}

output "account_id" {
  value = data.aws_caller_identity.current.account_id
}

output "caller_arn" {
  value = data.aws_caller_identity.current.arn
}

output "caller_user" {
  value = data.aws_caller_identity.current.user_id
}
