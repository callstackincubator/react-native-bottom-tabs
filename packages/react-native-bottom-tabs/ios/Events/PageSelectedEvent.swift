import React

@objcMembers
public class PageSelectedEvent: NSObject, RCTEvent {
  private var key: NSString
  public var viewTag: NSNumber

  public var eventName: String {
    "onPageSelected"
  }

  public init(reactTag: NSNumber, key: NSString) {
    self.viewTag = reactTag
    self.key = key
    super.init()
  }

  public class func moduleDotMethod() -> String {
    "RCTEventEmitter.receiveEvent"
  }

  public func canCoalesce() -> Bool {
      false
  }

  public func arguments() -> [Any] {
    [
      viewTag,
      RCTNormalizeInputEventName(eventName) ?? eventName,
      [
        "key": key
      ]
    ]
  }
}
