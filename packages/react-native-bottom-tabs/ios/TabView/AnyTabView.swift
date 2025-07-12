import SwiftUI

public protocol AnyTabView: View {
  var onLayout: (_ size: CGSize) -> Void { get }
  var onSelect: (_ key: String) -> Void { get }
  var updateTabBarAppearance: () -> Void { get }
}
