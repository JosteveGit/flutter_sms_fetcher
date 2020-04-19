#import "SmsfetcherPlugin.h"
#if __has_include(<smsfetcher/smsfetcher-Swift.h>)
#import <smsfetcher/smsfetcher-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "smsfetcher-Swift.h"
#endif

@implementation SmsfetcherPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftSmsfetcherPlugin registerWithRegistrar:registrar];
}
@end
