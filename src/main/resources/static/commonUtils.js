
function isLoggedIn() {
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
    //var userDisplayName = getCookie('userDisplayName');
    if (userInfo.isLoggedIn) {
        $(div).text("Hello " + userInfo.displayName + "!");
        //alert('Here');
    } else {
        //alert('There');
        var redirectUrl = "&redirect_uri=" + encodeURIComponent(window.location.origin + "/login");
        var state = "&state=" + encodeURIComponent(document.URL);
        //
        //var href = baseUrl + path + standardParams + redirectUrl + state;
        var href = getCookie("loginUrl") + redirectUrl + state;
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
function sendData(author, appName, appData) {
    $.post(
        {
            url: "/appState/" + author + "/" + appName,
            data: {appData: JSON.stringify(appData)},
            success: function(data, status) {
                console.log("ZZZ sendData - " + status);
            }
        }
    );
}
function getLoginInformation() {
}