import SwiftUI
import React

@available(iOS 18, macOS 15, visionOS 2, tvOS 18, *)
struct NewTabView: AnyTabView {
  @ObservedObject var props: TabViewProps

  var onLayout: (CGSize) -> Void
  var onSelect: (String) -> Void
  var updateTabBarAppearance: () -> Void

  @ViewBuilder
  var body: some View {
    TabView(selection: $props.selectedPage) {
      ForEach(props.children) { child in
        if let index = props.children.firstIndex(of: child),
           let tabData = props.items[safe: index] {
          let isFocused = props.selectedPage == tabData.key

          if !tabData.hidden || isFocused {
            let icon = props.icons[index]

            let context = TabAppearContext(
              index: index,
              tabData: tabData,
              props: props,
              updateTabBarAppearance: updateTabBarAppearance,
              onSelect: onSelect
            )

            Tab(value: tabData.key, role: tabData.role?.convert()) {
              RepresentableView(view: child.view)
                .ignoresSafeArea(.container, edges: .all)
                .tabAppear(using: context)
                .hideTabBar(props.tabBarHidden)
            } label: {
              TabItem(
                title: tabData.title,
                icon: icon,
                sfSymbol: tabData.sfSymbol,
                labeled: props.labeled
              )
            }
            #if !os(tvOS)
            .badge(tabData.badge.flatMap { !$0.isEmpty ? Text($0) : nil })
            #endif
            .accessibilityIdentifier(tabData.testID ?? "")
          }
        }
      }
    }
    .measureView { size in
      onLayout(size)
    }
    .modifier(ConditionalBottomAccessoryModifier(props: props))
  }
}

struct ConditionalBottomAccessoryModifier: ViewModifier {
  let props: TabViewProps
  
  // Check if there's a bottom accessory component view
  private var hasBottomAccessory: Bool {
    props.children.contains { child in
      let className = String(describing: type(of: child.view))
      return className == "RCTBottomAccessoryComponentView"
    }
  }
  
  // Find the bottom accessory view
  private var bottomAccessoryView: PlatformView? {
    props.children.first { child in
      let className = String(describing: type(of: child.view))
      return className == "RCTBottomAccessoryComponentView"
    }?.view
  }
  
  func body(content: Content) -> some View {
    if #available(iOS 26.0, macOS 26.0, tvOS 26.0, visionOS 3.0, *) {
      if hasBottomAccessory {
        content
          .tabViewBottomAccessory {
            renderBottomAccessoryView()
          }
      } else {
        content
      }
    } else {
      content
    }
  }
  
  @ViewBuilder
  private func renderBottomAccessoryView() -> some View {
    if let accessoryView = bottomAccessoryView {
      BottomAccessoryRepresentableView(view: accessoryView)
    }
  }
}

struct BottomAccessoryRepresentableView: PlatformViewRepresentable {
  var view: PlatformView

  func makeUIView(context: Context) -> PlatformView {
    return view
  }

  func updateUIView(_ uiView: PlatformView, context: Context) {}
}
