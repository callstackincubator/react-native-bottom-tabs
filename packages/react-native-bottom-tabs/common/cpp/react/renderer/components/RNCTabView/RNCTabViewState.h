#ifdef __cplusplus

#pragma once

#include <react/renderer/core/StateData.h>
#ifdef RN_SERIALIZABLE_STATE
#include <folly/dynamic.h>
#endif

namespace facebook::react {

class RNCTabViewState final {
 public:
  RNCTabViewState() = default;

  void setImageLoader(std::weak_ptr<void> imageLoader);
  std::weak_ptr<void> getImageLoader() const noexcept;

 private:
  std::weak_ptr<void> imageLoader_;
};

} // namespace facebook::react

#endif
