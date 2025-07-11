import SwiftUI

/**
 Helper used to render UIView inside of SwiftUI.
 */
struct RepresentableView: PlatformViewRepresentable {
  var view: PlatformView

#if os(macOS)

  func makeNSView(context: Context) -> PlatformView {
    view
  }

  func updateNSView(_ nsView: PlatformView, context: Context) {}

#else

  func makeUIView(context: Context) -> PlatformView {
    view
  }

  func updateUIView(_ uiView: PlatformView, context: Context) {}

#endif
}
