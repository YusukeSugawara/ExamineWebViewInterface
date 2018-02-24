# ExamineWebViewInterface

*work in progress*

## Overview

This study project compares Native/JS interfaces on cross-platfrom WebViews from the perspective about easier implementation.

## Test

### Targets

* Android (uses [WebView](https://developer.android.com/reference/android/webkit/WebView.html))
* iOS (uses [WKWebView](https://developer.apple.com/documentation/webkit/wkwebview))

### Interface Design Patterns

#### Custom URL scheme

* When callback from JS to Native, loads a dummy URL having own URL scheme, event name, and parameters.

##### Native --> JS 

When call JS methods from Native:

1. Native evaluates JavaScript code.
    * **Android:**
    [WebView.evaluateJavascript(script, resultCallback)](https://goo.gl/DCbxP8)
    * **iOS:** *under examination*
    [WKWebView.evaluateJavaScript(_:completionHandler:)](https://developer.apple.com/documentation/webkit/wkwebview/1415017-evaluatejavascript)
2. Native receives the calling directly, handles it.


##### JS --> Native

When notify event having some parameters from JS to Native:

1. JS sets `{customURLScheme}://{eventName}/?{parameters}` to `location.href`.
2. Native hooks the loading request event, checks whether or not url format has custom URL scheme(`{customURLScheme}://`).
    * **Android:**
    [WebViewClient.shouldOverrideUrlLoading(view, url)](https://goo.gl/K16Yq8)
    * **iOS:** *under examination*
    [WKNavigationDelegate.webView:decidePolicyForNavigationAction:decisionHandler:](https://developer.apple.com/documentation/webkit/wknavigationdelegate/1455641-webview?language=objc)
    
3. If is true, Native parses `eventName` and `parameters`, handles it.


#### [Apache Cordova](https://cordova.apache.org/)

*under examination*

#### Other ways

*under examination*


### Pros/Cons

#### Custom URL scheme

*under examination*

#### Apache Cordova

*under examination*
