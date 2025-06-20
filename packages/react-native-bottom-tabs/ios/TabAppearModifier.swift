import SwiftUI

struct TabAppearContext {
  let index: Int
  let tabData: TabInfo
  let props: TabViewProps
  let updateTabBarAppearance: () -> Void
  let onSelect: (_ key: String) -> Void
}

struct TabAppearModifier: ViewModifier {
  let context: TabAppearContext
  
  func body(content: Content) -> some View {
    content.onAppear {
#if !os(macOS)
      context.updateTabBarAppearance()
#endif
      
#if os(iOS)
      if context.index >= 4, context.props.selectedPage != context.tabData.key {
        context.onSelect(context.tabData.key)
      }
#endif
    }
  }
}

extension View {
  func tabAppear(using context: TabAppearContext) -> some View {
    self.modifier(TabAppearModifier(context: context))
  }
}
