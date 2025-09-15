#include <react/renderer/components/RNCTabView/RNCTabViewState.h>

namespace facebook::react {

void RNCTabViewState::setImageLoader(
    std::weak_ptr<void> imageLoader) {
  imageLoader_ = imageLoader;
}

std::weak_ptr<void> RNCTabViewState::getImageLoader()
    const noexcept {
  return imageLoader_;
}

} // namespace facebook::react
