import 'package:flutter/services.dart';

class OkSpinPlugin {
  // 创建通道对象，与android端创建的通道对象名称一致
  static const MethodChannel _channel =
      MethodChannel('plugins.flutter.io/okspin_plugin');
  static MethodChannel get channel => _channel;

  static Future<bool> initSDK() async {
    return await _retry(() => _channel.invokeMethod<bool>('initSDK'));
  }

  static Future<bool> getPlacement() async {
    return await _retry(() => _channel.invokeMethod<bool>('getPlacement'));
  }

  static Future<bool> openGSpace() async {
    return await _retry(() => _channel.invokeMethod<bool>('openGSpace'));
  }

  static Future<bool> openInteractiveAds() async {
    return await _retry(
        () => _channel.invokeMethod<bool>('openInteractiveAds'));
  }

  static Future<bool> notifyGSPubTaskPayout(List<dynamic> records) async {
    return await _retry(
        () => _channel.invokeMethod<bool>('notifyGSPubTaskPayout', records));
  }

  static Future<String> getUserId() async {
    return await _channel.invokeMethod<String>('getUserId') ?? '';
  }

  static Future<bool> setUserId(String userId) async {
    return await _retry(() => _channel.invokeMethod<bool>('setUserId', userId));
  }

  static Future<bool> openOfferWall() async {
    return await _retry(() => _channel.invokeMethod<bool>('openOfferWall'));
  }

  static Future<bool> notifyOfferWallPayout() async {
    return await _retry(
        () => _channel.invokeMethod<bool>('notifyOfferWallPayout'));
  }

  static Future<bool> _retry(Future<bool?> Function() doThings) async {
    int retryCount = 0;
    bool isRetry = true;
    bool result = false;
    while (isRetry && retryCount < 3) {
      try {
        result = await doThings() ?? false;
        isRetry = false;
      } catch (error) {
        if (retryCount < 3) {
          retryCount++;
          await Future.delayed(const Duration(milliseconds: 1000));
          continue;
        }
        isRetry = false;
      }
    }
    return result;
  }
}
