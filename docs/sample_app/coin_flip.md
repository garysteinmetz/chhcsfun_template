# The Coin Flip Game

[Click here to go back to the main page.](../../README.md)

## Goal

Run a local application server that will host a game that keeps track of how
many times the user has consecutively flipped a coin on 'heads' . The user
can login and the user will be able to keep and update the same high score
even when switching browsers.

Note that the configurations set forth below will run the application without any
communication with AWS.

## Download, Assemble, and Run Application Server

1) Open a command prompt and go to base directory of this project
(where the `README.md` file is)
2) Set necessary environment variables for using this application by following
the steps in the 'Set Environment Variables' section below
3) Using Maven, assemble the project with command `mvn clean install`
4) Run the project with command `java -jar ./target/demo-0.0.1-SNAPSHOT.jar`
5) Go to `http://localhost:8080/` , click 'Login Now', then start playing the game
by continuously clicking the 'Flip Coin!' button
6) Open either a different browser (e.g. Safari if Chrome was used in the previous step)
or an incognito browser, then repeat the last step and notice that the high score
('Your best-ever score') was carried over to this browser

### Set Environment Variables

In a command prompt, enter these commands.

#### On Windows

Enter these commands -

```
set LOCAL_CMS_PATH=.\examples\coin_flip_game\
set LOCAL_IAM_USER={"name":"Biff the Understudy"}
set LOCAL_USER_DATA={"isPresent":false}
```

Then enter `set` to get a listing of all environment variables and then confirm that these
ones are set correctly.

#### On MacOS


Enter these commands -

```
export LOCAL_CMS_PATH=./examples/coin_flip_game/
export LOCAL_IAM_USER="{\"name\":\"Biff the Understudy\"}"
export LOCAL_USER_DATA="{\"isPresent\":false}"
```

Then enter `export` to get a listing of all environment variables and then confirm that these
ones are set correctly.

[Click here to go back to the main page.](../../README.md)
