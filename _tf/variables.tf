variable "APP_OS_USER" {
  type = string
  default = "ubuntu"
}
variable "APP_PORTS" {
  type = string
  default = "fromPort=22,toPort=22,protocol=tcp fromPort=8080,toPort=8080,protocol=tcp"
}
variable "AWS_COGNITO_CLIENT_ID" {
  type = string
}
variable "AWS_COGNITO_CLIENT_SECRET" {
  type = string
}
variable "AWS_COGNITO_GROUP_DEVELOPERS" {
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
variable "AWS_DYNAMODB_TABLE_NAME_USERAPPDATA" {
  type = string
}
variable "AWS_S3_BUCKET_NAME_CONTENT" {
  type = string
}
variable "AWS_S3_BUCKET_PERUSERLIMIT" {
  type = string
}
