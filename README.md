# ExamineWebViewInterface

*work in progress*

## Overview

This study project compares Native/JS interfaces on cross-platfrom WebViews from the perspective about easier implementation.

## Test

### Targets

* Android (uses [WebView](https://developer.android.com/reference/android/webkit/WebView.html))
* iOS (uses [WKWebView](https://developer.apple.com/documentation/webkit/wkwebview))

### Interface Design Patterns

#### raw-webview-api

##### Android

* If call to JS from Java:
    1. Java evaluates JavaScript code directly with [WebView.evaluateJavascript(script, resultCallback)](https://goo.gl/DCbxP8).
    2. JS can receive it as a normally function calling.

* If callback to Java from JS:
    1. In advance, Java registers a [WebViewClient](https://developer.android.com/reference/android/webkit/WebViewClient.html) to WebView.
    2. JS sets `{customURLScheme}://{eventName}/?{parameters}` to `location.href`.
        * The `{customURLScheme}://` is the marker to classify a loading event as a callback.
        * The `/?` part separates `{eventName}` and `{parameters}`.
        If there is one event type only and JS don't need to think about event types, simply putting `{parameters}` part is also OK.
    3. Java can receive it as a URL loading event which can listen with [WebViewClient.shouldOverrideUrlLoading(view, url)](https://goo.gl/K16Yq8).

###### WARNING

I recommend we **don't use** [WebView.addJavascriptInterface(Object object, String name)](https://goo.gl/uskdfy).
It ables to call Java method from JS directly, but [this pattern is an ex‐convict](https://stackoverflow.com/questions/6415882/android-javascriptinterface-security).  So it can bring unknown security risks.


##### iOS

* If call to JS from Swift:
    1. Swift evaluates JavaScript code directly with [WKWebView.evaluateJavaScript(_:completionHandler:)](https://developer.apple.com/documentation/webkit/wkwebview/1415017-evaluatejavascript).
    2. JS can receive it as a normally function calling.

* If callback to Swift from JS:
    1. In advance, Swift registers a [WKWebViewConfiguration](https://developer.apple.com/documentation/webkit/wkwebviewconfiguration) to WKWebView by using [init(frame:configuration:)](https://developer.apple.com/documentation/webkit/wkwebview/1414998-init).
    Then sets [WKUserContentController](https://developer.apple.com/documentation/webkit/wkusercontentcontroller) which added [WKScriptMessageHandler](https://developer.apple.com/documentation/webkit/wkscriptmessagehandler) by [WKUserContentController.add(_:name:)](https://developer.apple.com/documentation/webkit/wkusercontentcontroller/1537172-add).
    ```swift
    import Webkit

    let CALLBACK_HANDLER_NAME = "callbackHandler"

    let configuration = WKWebViewConfiguration()
    let userContentController = WKUserContentController()
    userContentController.add(self, name: CALLBACK_HANDLER_NAME)
    configuration.userContentController = userContentController
    
    let webView = WKWebView(frame: self.view.bounds, configuration: configuration)
    ```

    2. JS calls `webkit.messageHandlers.callbackHandler.postMessage(object)`.
    3. Swift can receive it as a message event which can listen with [WKScriptMessageHandler.userContentController(_:didReceive:)](https://developer.apple.com/documentation/webkit/wkscriptmessagehandler/1396222-usercontentcontroller).


#### [Apache Cordova](https://cordova.apache.org/)

*under examination*

#### Other ways

*under examination*


### Pros/Cons

#### Custom URL scheme

*under examination*

#### Apache Cordova

*under examination*
