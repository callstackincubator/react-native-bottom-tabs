import React

@objcMembers
public class TabBarMeasuredEvent: NSObject, RCTEvent {
  private var height: NSInteger
  public var viewTag: NSNumber

  public var eventName: String {
    "onTabBarMeasured"
  }

  public init(reactTag: NSNumber, height: NSInteger) {
    self.viewTag = reactTag
    self.height = height
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
        "height": height
      ]
    ]
  }
}
