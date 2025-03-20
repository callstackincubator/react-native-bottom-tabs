module.exports = {
  dependency: {
    platforms: {
      android: {
        libraryName: 'RNCTabView',
        componentDescriptors: ['RNCTabViewScreenComponentDescriptor'],
        cmakeListsPath: 'src/main/jni/CMakeLists.txt',
      },
    },
  },
};
