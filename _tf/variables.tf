variable "APP_OS_USER" {
  type = string
  default = "ubuntu"
}
variable "APP_PORTS" {
  type = string
  default = "fromPort=22,toPort=22,protocol=tcp fromPort=80,toPort=80,protocol=tcp fromPort=443,toPort=443,protocol=tcp"
}
#
#
#
variable "AWS_ACCOUNT_ID" {
  type = string
}
variable "AWS_REGION" {
  type = string
}
variable "AWS_AVAILABILITY_ZONE" {
  type = string
  default = ""
}
variable "AWS_DOMAIN_NAME" {
  type = string
}
variable "AWS_S3_BUCKET_NAME_CONTENT" {
  type = string
}
variable "AWS_DYNAMODB_TABLE_NAME_USERAPPDATA" {
  type = string
}
variable "AWS_COGNITO_CLIENT_ID" {
  type = string
}
variable "AWS_COGNITO_CLIENT_SECRET" {
  type = string
}
variable "AWS_COGNITO_DOMAIN_NAME" {
  type = string
}
variable "AWS_COGNITO_OAUTH2_AUTHORIZE_URL" {
  type = string
}
variable "AWS_COGNITO_OAUTH2_TOKEN_URL" {
  type = string
}
variable "AWS_COGNITO_USER_POOL_ID" {
  type = string
}
variable "AWS_DEVOPS_CONSOLE_URL" {
  type = string
}
variable "AWS_DEVOPS_USERNAME" {
  type = string
}
variable "AWS_DEVOPS_PASSWORD" {
  type = string
}
variable "AWS_DEVOPS_ACCESS_KEY_ID" {
  type = string
}
variable "AWS_DEVOPS_SECRET_ACCESS_KEY" {
  type = string
}
