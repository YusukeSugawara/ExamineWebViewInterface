//
//  Lagdoll.swift
//  ExamineWebViewInterface-ios
//

import Foundation

class Lagdoll {
    private static func logOutput(message: String,
                            filePath: String,
                            functionName: String,
                            marker: String) {
        let tag = (filePath as NSString).lastPathComponent
        NSLog("[\(tag) \(functionName)] \(marker)\(marker.count==0 ? "" : " ")\(message)")
    }
    
    static func d(_ message: String,
                  _ filePath: String = #file,
                  _ functionName: String = #function) {
        logOutput(message: message, filePath: filePath, functionName: functionName, marker: "")
    }

    static func w(_ message: String,
                  _ filePath: String = #file,
                  _ functionName: String = #function) {
        logOutput(message: message, filePath: filePath, functionName: functionName, marker: "😨")
    }

    static func e(_ message: String,
                  _ filePath: String = #file,
                  _ functionName: String = #function) {
        logOutput(message: message, filePath: filePath, functionName: functionName, marker: "😡")
    }
}
