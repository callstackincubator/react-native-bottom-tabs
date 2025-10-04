import React
#if os(macOS)
import AppKit
#else
import UIKit
#endif

enum TabBarFontSize {
  /// Returns the default font size for tab bar item labels based on the current platform
#if os(tvOS)
  static let defaultSize: CGFloat = 30.0
#elseif os(macOS)
  static let defaultSize: CGFloat = 11.0
#else
  static let defaultSize: CGFloat = {
    if UIDevice.current.userInterfaceIdiom == .pad {
      return 13.0
    }

    return 10.0
  }()
#endif

  /// Creates font attributes for tab bar items
  /// - Parameters:
  ///   - size: Font size in points
  ///   - family: Optional font family name
  ///   - weight: Optional font weight string
  ///   - color: Optional text color
  /// - Returns: Dictionary of NSAttributedString attributes
  static func createFontAttributes(
    size: CGFloat,
    family: String? = nil,
    weight: String? = nil,
    color: PlatformColor? = nil
  ) -> [NSAttributedString.Key: Any] {
    var attributes: [NSAttributedString.Key: Any] = [:]

    // Create font with React Native font handling if family or weight is specified
    if family != nil || weight != nil {
      attributes[.font] = RCTFont.update(
        nil,
        withFamily: family,
        size: NSNumber(value: size),
        weight: weight,
        style: nil,
        variant: nil,
        scaleMultiplier: 1.0
      )
    } else {
      // Fallback to system font
      #if os(macOS)
        attributes[.font] = NSFont.boldSystemFont(ofSize: size)
      #else
        attributes[.font] = UIFont.boldSystemFont(ofSize: size)
      #endif
    }

    // Add color if provided
    if let color {
      attributes[.foregroundColor] = color
    }

    return attributes
  }

  /// Creates font attributes specifically for normal (unselected) tab state
  /// - Parameters:
  ///   - fontSize: Optional font size (uses default if nil)
  ///   - fontFamily: Optional font family
  ///   - fontWeight: Optional font weight
  ///   - inactiveColor: Optional color for inactive state
  /// - Returns: Font attributes dictionary
  static func createNormalStateAttributes(
    fontSize: Int? = nil,
    fontFamily: String? = nil,
    fontWeight: String? = nil,
    inactiveColor: PlatformColor? = nil
  ) -> [NSAttributedString.Key: Any] {
    let size = fontSize.map(CGFloat.init) ?? defaultSize
    return createFontAttributes(
      size: size,
      family: fontFamily,
      weight: fontWeight,
      color: inactiveColor
    )
  }
}
