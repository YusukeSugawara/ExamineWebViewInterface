const CUSTOM_URL_SCHEME = "work.yusukesugawara.callback://";

var elementHello = null;

window.onload = function() {
    document.body.insertAdjacentHTML('afterbegin', '<div id="hello"></div>');

    elementHello = document.getElementById('hello');

    callbackToNative("ready");
};

function callbackToNative(eventName, parameters) {
    if (parameters == null) {
        parameters = {};
    }

    /*
    Detecting iOS / Android Operating system
    https://stackoverflow.com/a/21742107
    */
   
    var userAgent = navigator.userAgent || navigator.vendor || window.opera;

    // Windows Phone must come first because its UA also contains "Android"
    if (/windows phone/i.test(userAgent)) {
        // Not supports in my App
        return;
    }

    if (/android/i.test(userAgent)) {
        window.location.href = CUSTOM_URL_SCHEME + eventName + '/?' + JSON.stringify(parameters);
        return;
    }

    // iOS detection from: http://stackoverflow.com/a/9039885/177710
    if (/iPad|iPhone|iPod/.test(userAgent) && !window.MSStream) {
        // TODO: Implement iOS logics

        /*
        UIWebView and JavaScriptInterface in Swift
        https://stackoverflow.com/a/37373745
        */
        return;
    }

    // other OS (Development environment)
    hello(new Date().getTime());
}

function hello(timeMillis) {
    if (elementHello == null) {
        return;
    }

    var date = new Date(timeMillis);
    elementHello.innerHTML = "hello: " + date.toISOString();

    return elementHello.innerHTML;
}
