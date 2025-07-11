import UIKit

enum TabBarFontSize {
  /// Returns the default font size for tab bar item labels based on the current platform
#if os(tvOS)
  static var defaultSize: CGFloat = 30.0
#else
  static var defaultSize: CGFloat = {
    if UIDevice.current.userInterfaceIdiom == .pad {
      return 13.0
    }
    
    return 10.0
  }()
#endif
}
