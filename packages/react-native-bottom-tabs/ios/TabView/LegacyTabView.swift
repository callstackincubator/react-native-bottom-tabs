import SwiftUI

struct LegacyTabView: View {
  @ObservedObject var props: TabViewProps

#if os(macOS)
  var tabBar: NSTabView?
#else
  var tabBar: UITabBar?
#endif
  
  var onLayout: (_ size: CGSize) -> Void
  var onSelect: (_ key: String) -> Void
  var updateTabBarAppearance: (_ props: TabViewProps, _ tabBar: UITabBar?) -> Void
  
  @ViewBuilder
  var body: some View {
    TabView(selection: $props.selectedPage) {
      ForEach(props.children.indices, id: \.self) { index in
        renderTabItem(at: index)
      }
      .measureView { size in
        onLayout(size)
      }
    }
    .hideTabBar(props.tabBarHidden)
  }
  
  @ViewBuilder
  private func renderTabItem(at index: Int) -> some View {
    if let tabData = props.items[safe: index] {
      let isFocused = props.selectedPage == tabData.key
      
      if !tabData.hidden || isFocused {
        let icon = props.icons[index]
        let child = props.children[safe: index] ?? PlatformView()
        
        RepresentableView(view: child)
          .ignoresSafeArea(.container, edges: .all)
          .tabItem {
            TabItem(
              title: tabData.title,
              icon: icon,
              sfSymbol: tabData.sfSymbol,
              labeled: props.labeled
            )
            .accessibilityIdentifier(tabData.testID ?? "")
            .tag(tabData.key)
            .tabBadge(tabData.badge)
            .onAppear {
#if !os(macOS)
              updateTabBarAppearance(props, tabBar)
#endif
#if os(iOS)
              if index >= 4, !isFocused {
                onSelect(tabData.key)
              }
#endif
            }
          }
      }
    }
  }
}
