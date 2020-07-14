#import "FlutterPeluhttpPlugin.h"
#if __has_include(<flutter_peluhttp/flutter_peluhttp-Swift.h>)
#import <flutter_peluhttp/flutter_peluhttp-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_peluhttp-Swift.h"
#endif

@implementation FlutterPeluhttpPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterPeluhttpPlugin registerWithRegistrar:registrar];
}
@end
