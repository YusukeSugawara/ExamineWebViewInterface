//
//  ViewController.swift
//  ExamineWebViewInterface-ios
//

import UIKit
import WebKit

class ViewController: UIViewController, WKScriptMessageHandler {
    weak var webView: WKWebView?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        self.configureWebView()
        
        guard let path = Bundle.main.path(forResource: "ExamineWebViewInterface-web/index", ofType: "html") else {
            Lagdoll.e("resource not found!")
            return
        }
        
        let url = URL(fileURLWithPath: path)
        let urlRequest = URLRequest(url: url)
        self.webView?.load(urlRequest)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    let CALLBACK_HANDLER_NAME = "callbackHandler"
    
    private func configureWebView() {
        Lagdoll.d("");
        
        let configuration = WKWebViewConfiguration()
        let userContentController = WKUserContentController()
        userContentController.add(self, name: CALLBACK_HANDLER_NAME)
        configuration.userContentController = userContentController
        
        let webView = WKWebView(frame: self.view.bounds, configuration: configuration)
        self.view.addSubview(webView)
        self.webView = webView
    }
    
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        if message.name != CALLBACK_HANDLER_NAME {
            return
        }
        
        Lagdoll.d("message.body=\(message.body): \(type(of: message.body))")
        
        guard let body = message.body as? NSDictionary else {
            return
        }
        guard let eventName = body["eventName"] as? String else {
            return
        }
        guard let paramsJson = body["parameters"] as? NSDictionary else {
            return
        }
        
        Lagdoll.d("[\(eventName)] \(paramsJson)")
        
        switch eventName {
        case "ready":
            self.helloTimer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: true) { _ in
                self.callJSFunction()
            }
            break
        default:
            break
        }
    }
    
    var helloTimer: Timer?
    
    private func callJSFunction() {
        let timeMillis = NSDate().timeIntervalSince1970 * 1000
        
        Lagdoll.d("timeMillis=\(timeMillis)")
        
        self.webView?.evaluateJavaScript("hello(\(timeMillis));") { (_response, _error) in
            if let error = _error {
                Lagdoll.e("error=\(error)")
                return
            }
            
            guard let response = _response else {
                Lagdoll.e("no response")
                return
            }
            
            Lagdoll.d("response=\(response)")
        }
    }
}
