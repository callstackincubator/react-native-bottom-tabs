import Foundation
import SwiftUI
@_spi(Advanced) import SwiftUIIntrospect

#if os(macOS)
import AppKit
#else
import UIKit
#endif

extension Collection {
  // Returns the element at the specified index if it is within bounds, otherwise nil.
  subscript(safe index: Index) -> Element? {
    indices.contains(index) ? self[index] : nil
  }
}

extension Collection where Element == TabInfo {
  func findByKey(_ key: String?) -> Element? {
    guard let key else { return nil }
    guard !isEmpty else { return nil }
    return first { $0.key == key }
  }
}

extension PlatformView {
  func pinEdges(to other: PlatformView) {
    NSLayoutConstraint.activate([
      leadingAnchor.constraint(equalTo: other.leadingAnchor),
      trailingAnchor.constraint(equalTo: other.trailingAnchor),
      topAnchor.constraint(equalTo: other.topAnchor),
      bottomAnchor.constraint(equalTo: other.bottomAnchor)
    ])
  }
}

extension PlatformImage {
  func resizeImageTo(size: CGSize) -> PlatformImage? {
#if os(macOS)
    return NSImage(size: size, flipped: false) { rect -> Bool in
      self.draw(in: rect,
                from: CGRect(origin: .zero, size: self.size),
                operation: .copy,
                fraction: 1.0)
      return true
    }
#else
    let renderer = UIGraphicsImageRenderer(size: size)
    return renderer.image { _ in
      self.draw(in: CGRect(origin: .zero, size: size))
    }
#endif
  }
}

extension View {
#if os(macOS)
  @MainActor
  @ViewBuilder
  func introspectTabView(closure: @escaping (NSTabView) -> Void) -> some View {
    self
      .introspect(
        .tabView,
        on: .macOS(.v11...),
        customize: closure
      )
  }
#else
  @MainActor
  @ViewBuilder
  func introspectTabView(closure: @escaping (UITabBarController) -> Void) -> some View {
    self
#if !os(visionOS)
      .introspect(
        .tabView,
        on: .iOS(.v14...),
        .tvOS(.v14...),
        customize: closure
      )
#endif
  }
#endif

  @MainActor
  @ViewBuilder
  func measureView(onLayout: @escaping (_ size: CGSize) -> Void) -> some View {
    self
      .background(
        GeometryReader { geometry in
          Color.clear
            .onChange(of: geometry.size) { newValue in
              onLayout(newValue)
            }
            .onAppear {
              onLayout(geometry.size)
            }
        }
          .ignoresSafeArea(.all, edges: .all)
      )
  }
}
