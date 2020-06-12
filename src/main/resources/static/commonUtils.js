
function isLoggedIn() {
}
function generateLoginDomStructure(targetId) {
    var baseDiv = $("#" + targetId)
    var div = document.createElement( "div" );
    $(baseDiv).append(div);
    //
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
function getCookie(name) {
    var outValue = '';
    var re = new RegExp('[; ]' + name + '=([^\\s;]*)');
    var sMatch = (' ' + document.cookie).match(re);
    if (sMatch) {
        outValue = unescape(sMatch[1]);
    }
    return outValue;
}
function getLoginInformation() {
}