import SwiftUI

@available(iOS 18, macOS 15, visionOS 2, tvOS 18, *)
struct NewTabView: AnyTabView {
  @ObservedObject var props: TabViewProps
  
  var onLayout: (CGSize) -> Void
  var onSelect: (String) -> Void
  var updateTabBarAppearance: () -> Void

  @ViewBuilder
  var body: some View {
    TabView(selection: $props.selectedPage) {
      ForEach(props.children.indices, id: \.self) { index in
        if let tabData = props.items[safe: index] {
          let isFocused = props.selectedPage == tabData.key

          if !tabData.hidden || isFocused {
            let icon = props.icons[index]
            let role: TabRole? = nil

            let platformChild = props.children[safe: index] ?? PlatformView()
            let child = RepresentableView(view: platformChild)
            let context = TabAppearContext(
              index: index,
              tabData: tabData,
              props: props,
              updateTabBarAppearance: updateTabBarAppearance,
              onSelect: onSelect
            )

            Tab(value: tabData.key, role: role) {
              child
                .ignoresSafeArea(.container, edges: .all)
                .tabAppear(using: context)
            } label: {
              TabItem(
                title: tabData.title,
                icon: icon,
                sfSymbol: tabData.sfSymbol,
                labeled: props.labeled
              )
            }
            //.badge(tabData.badge)
            .accessibilityIdentifier(tabData.testID ?? "")
          }
        }
      }
    }
    .measureView { size in
      onLayout(size)
    }
    .hideTabBar(props.tabBarHidden)
  }
}
