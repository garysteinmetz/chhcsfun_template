
## Update Node

Go to https://nodejs.org/en/download/ , download the latest recommended ('LTS') version
of node, and install it (`macOS Installer (.pkg)`).

## Create React App

Run the following command and accept the default prompt values.

```
npx create-react-app arena-warrior
```

## Run Basic Project Commands

Go to the application directory (`cd arena-warrior`) and try the following commands.

Run the server.

```
npm run start
```

Run unit tests.

```
npm run test
```

## Build and Deploy Project

Run the following command to create (build) deployable components.
This will create deployable components (files) in the `build` directory.
(Note how there is a `/build` entry in the `.gitignore` file to prevent
these built components from being committed (saved) to a Git repository.
Derived components in a project generally aren't saved to source control.)

```
npm run build
```

### Deploy Project to External Server

Run this command to upload the web application to an external server.

```
aws s3 sync --exclude ".*" ./build s3://#S3_BUCKET_NAME#/content
```

## Add Phaser Library

Run the following command to download Phaser and add it as a dependency
in the `package.json` file.

```
npm install phaser --save
```