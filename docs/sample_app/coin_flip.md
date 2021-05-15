# Bonus: I Have Used the Online Authoring Tool to Create a Simple Game

## Goal

Run a local application server that will host a game that keeps track of how
many times the user has consecutively flipped a coin on 'heads' . The user
can login and the user will be able to keep and update the same high score
even when switching browsers.

## Setup

1) Open a command prompt and go to your desktop
2) Create a directory called 'content' by issuing command `mkdir content`
3) In this directory, create file `index.html` which will have the same
content as the file found in the "Create 'index.html' File" section below
4) In this same 'content' subdirectory, create two image files - one representing
the 'head' of a coin (named 'heads.png') and the other representing the 'tail' of the coin
(named 'tails.png')
5) Run the commands in the 'Download, Assemble, and Run Application Server' section below
6) Open a browser, go to `http://localhost:8080`, login, and start playing for a while
7) Open another browser, either a different brand or in 'incognito' mode,
go to `http://localhost:8080`, login, and start playing
8) Notice that the second browser recognized the high score of the first browser just
after logging in

## Download, Assemble, and Run Application Server

1) Open a command prompt (if you haven't already) and go to the desktop
2) Set necessary environment variables for using this application by following
the steps in the 'Set Environment Variables' section below
3) Download the application with command `git clone https://github.com/garysteinmetz/chhcsfun_template.git`
4) Enter `cd chhcsfun_template` to go into that project's base directory
5) Using Maven, assemble the project with command `mvn clean install`
6) Run the project with command `java -jar ./target/demo-0.0.1-SNAPSHOT.jar`

### Set Environment Variables

In a command prompt, enter these commands. _Note that for 'LOCAL_CMS_PATH' the
value should be the directory path value representing the directory for your desktop.
The value shown here is just an example._

#### On Windows

Enter these commands -

```
set LOCAL_CMS_PATH="/Users/garysteinmetz/Desktop"
set LOCAL_IAM_USER="{\"name\":\"Biff the Understudy\"}"
set LOCAL_USER_DATA="{\"isPresent\":false}"
```

Then enter `set` to get a listing of all environment variables and then confirm that these
ones are set correctly.

#### On MacOS


Enter these commands -

```
export LOCAL_CMS_PATH="/Users/garysteinmetz/Desktop"
export LOCAL_IAM_USER="{\"name\":\"Biff the Understudy\"}"
export LOCAL_USER_DATA="{\"isPresent\":false}"
```

Then enter `export` to get a listing of all environment variables and then confirm that these
ones are set correctly.

## How to Create Images

Go to https://jspaint.app/ or use the local Windows 'Paint' program to create
two images - the 'head' side of a coin and the 'tail' side of a coin.

### Set Dimension Boundaries and (Optionally) Make the Background Transparent

In the menu, click 'Image' then 'Attributes' then do the following.

  - For 'Units', select 'Pixels'
  - For 'Width', enter the desired width (like 100) in pixels
  - For 'Height', enter the desired height (like 100) in pixels
  - (Optional) To make sure that the unpainted parts of the image don't cover
  up what's behind it, select 'Transparent' under 'Transparency'

Once this is done, click the 'Okay' button.

  - If you selected the 'Transparency' button, clear the existing (white) background
  by selecting 'Image' then 'Clear Image' . The image should now look somewhat like
  a checkerboard (this is the representation of a transparent background,
  but it (the unpainted parts) will be 'see through' once rendered on a web page).

### Create Two Images - One of the Head Side of a Coin, One of the Tail Side of a Coin

Create two images - one of the head side of a coin, one of the tail side of a coin.

These images should be your artistic creation and does not have to represent
an actual coin in real life.

These images will have to be created one-at-a-time.

Save each file by clicking 'File' then 'Save' . Each saved file will have the name
'untitled.png' but should be named 'heads.png' and 'tails.png' for the head side and
tail side, respectively.

After saving the first image, click 'File' then 'New' to reset the canvas to start
working on the second image (don't forget to reset the boundaries and (optionally)
choose to enable transparency).

### Create 'index.html' File
Create a file named 'index.html' with the following content.

```
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script>
        var game = 'coinflipgame';
        var allPlayerData = [];
        var user;
        var topScore = -1;
        var lastGameScore = -1;
        var score = 0;
        function flipCoin() {
            var flipResult = getRandomWholeNumberUnder(2);
            if (flipResult === 1) {
                $("#coin_flip").attr("src", "heads.png");
                score++;
            } else {
                $("#coin_flip").attr("src", "tails.png");
                lastGameScore = score;
                score = 0;
                if (lastGameScore > topScore) {
                    topScore = lastGameScore;
                    if (user && user.isLoggedIn) {
                        sendData(game, {score: topScore}, null);
                    }
                }
            }
            updateHighScores();
        }
        function updateHighScores() {
            var highScoresEle = $("#high_scores");
            $(highScoresEle).empty();
            //
            if (lastGameScore >= 0) {
                var span1 = document.createElement( "span" );
                $(highScoresEle).append(span1);
                $(span1).text("The score of your last game was " + lastGameScore);
                var br1 = document.createElement( "br" );
                $(highScoresEle).append(br1);
            }
            if (topScore >= 0) {
                var span2 = document.createElement( "span" );
                $(highScoresEle).append(span2);
                $(span2).text("Your best-ever score is " + topScore);
                var br2 = document.createElement( "br" );
                $(highScoresEle).append(br2);
            }
            var span3 = document.createElement( "span" );
            $(highScoresEle).append(span3);
            $(span3).text("The score of your current game is " + score);
            var br3 = document.createElement( "br" );
            $(highScoresEle).append(br3);
        }
        //
        //
        //
        function isLoggedIn() {
        }
        function getUserInfo(callback) {
            $.get(
                {
                    url: "/userInfo",
                    success: function(data, status) {
                        if (callback) {
                            callback(data, status);
                        }
                    }
                }
            );
        }
        function generateLoginDomStructure(targetId) {
            $.get(
                {
                    url: "/userInfo",
                    success: function(data, status) {
                        generateLoginDomStructureImpl(targetId, data);
                    }
                }
            );
        }
        function generateLoginDomStructureImpl(targetId, userInfo) {
            var baseDiv = $("#" + targetId)
            var div = document.createElement( "div" );
            $(baseDiv).append(div);
            //
            if (userInfo.isLoggedIn) {
                $(div).text("Hello " + userInfo.displayName + "!");
            } else {
                var href = window.location.origin + "/login?url=" + encodeURIComponent(document.URL);
                var a = document.createElement( "a" );
                $(div).append(a);
                $(a).attr("href", href);
                $(a).text("Login Now");
            }
        }
        function getCookie(name) {
            var outValue = '';
            var re = new RegExp('[; ]' + name + '=([^\\s;]*)');
            var sMatch = (' ' + document.cookie).match(re);
            if (sMatch) {
                outValue = unescape(sMatch[1]);
            }
            return outValue;
        }
        function sendData(appName, appData, callback) {
            $.post(
                {
                    url: "/appState/" + appName,
                    data: {appData: JSON.stringify(appData)},
                    success: function(data, status) {
                        if (callback) {
                            callback(data, status);
                        }
                    }
                }
            );
        }
        function getData(appName, callback) {
            $.get(
                {
                    url: "/appState/" + appName,
                    success: function(data, status) {
                        if (callback) {
                            callback(data, status);
                        }
                    }
                }
            );
        }
        function getRandomWholeNumberUnder(range) {
            return Math.floor(Math.random()*range);
        }
        function randomizeArray(input) {
            var copyOfInput = [];
            for (var i = 0; i < input.length; i++) {
                copyOfInput.push(input[i]);
            }
            var output = [];
            for (var i = 0; i < input.length; i++) {
                var nextIndex = getRandomWholeNumberUnder(copyOfInput.length);
                output.push(copyOfInput[nextIndex]);
                copyOfInput.splice(nextIndex, 1);
            }
            return output;
        }
        function getLoginInformation() {
        }
    </script>
    <script>
          //
            $(document).ready(
                function() {
                    getUserInfo(
                        function(data, status) {
                            generateLoginDomStructureImpl("user_pane", data);
                            if (data.isLoggedIn) {
                                user = data;
                                getData(
                                    game,
                                    function(data, status) {
                                        if (data.isPresent) {
                                            var appData = JSON.parse(data.appData);
                                            if (appData.score) {
                                                topScore = appData.score;
                                            }
                                        }
                                    }
                                );
                            }
                        }
                    );
                }
            );
          //
      </script>
</head>
<body>
<hr />
<div style="align: center">
    <h1>Coin Flip Game</h1>
    <div style="align: center">
        Try to get as many consecutive 'heads' flips as you can!
    </div>
    <hr />
</div>
<div id="user_pane"></div>
<div id="high_scores" style="align: center">
</div>
<div>
    <hr />
    <input type="button" onclick="flipCoin()" value="Flip Coin!" />
    <br />
    <img id="coin_flip" src="" />
    <br />
</div>
</body>
</html>
```