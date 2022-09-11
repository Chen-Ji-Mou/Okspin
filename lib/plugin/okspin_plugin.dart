import 'package:flutter/services.dart';

class OkSpinPlugin {
  // 创建通道对象，与android端创建的通道对象名称一致
  static const MethodChannel _channel =
      MethodChannel('plugins.flutter.io/okspin_plugin');
  static MethodChannel get channel => _channel;

  static Future<dynamic> initSDK() async {
    return await _channel.invokeMethod('initSDK');
  }

  static Future<dynamic> getEntryView() async {
    return await _channel.invokeMethod('getEntryView');
  }
}
