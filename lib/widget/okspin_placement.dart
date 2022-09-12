import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:okspin/plugin/okspin_plugin.dart';

class OkSpinPlacementWidget extends StatefulWidget {
  const OkSpinPlacementWidget(
      {Key? key,
      required this.userId,
      required this.placementBuilder,
      this.handleGSpaceRewards})
      : super(key: key);

  final String userId;
  final WidgetBuilder placementBuilder;
  final Future<void> Function(List<dynamic> records)? handleGSpaceRewards;

  @override
  State<StatefulWidget> createState() => _OkSpinPlacementState();
}

class _OkSpinPlacementState extends State<OkSpinPlacementWidget> {
  bool jumping = false;
  Future<bool>? getPlacementFuture;

  @override
  void initState() {
    super.initState();
    OkSpinPlugin.channel.setMethodCallHandler(channelCallback);
    OkSpinPlugin.setUserId(widget.userId).then((value) {
      Fluttertoast.showToast(
          msg: value ? 'setUserId success' : 'setUserId failed');
    });
  }

  @override
  void dispose() {
    OkSpinPlugin.channel.setMethodCallHandler(null);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: jumping
          ? const CircularProgressIndicator(color: Colors.white)
          : buildPlacement(),
    );
  }

  Widget buildPlacement() {
    return LayoutBuilder(builder: (context, constraints) {
      return FutureBuilder<bool>(
        future: getPlacementFuture ??= OkSpinPlugin.getPlacement(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.done) {
            bool success = snapshot.data ?? false;
            Fluttertoast.showToast(
                msg: success ? 'getPlacement success' : 'getPlacement failed');
            if (success) {
              return GestureDetector(
                onTap: changeJumpStatus,
                child: AndroidView(
                  viewType: 'plugins.flutter.io/okspin_placement',
                  creationParams: <String, int>{
                    "width": constraints.maxWidth.toInt(),
                    "height": constraints.maxHeight.toInt(),
                  },
                  creationParamsCodec: const StandardMessageCodec(),
                ),
              );
            } else {
              return buildDefaultPlacement();
            }
          } else {
            return const CircularProgressIndicator(color: Colors.white);
          }
        },
      );
    });
  }

  Widget buildDefaultPlacement() {
    return InkWell(
      radius: 0,
      highlightColor: Colors.transparent,
      onTap: () {
        changeJumpStatus();
        OkSpinPlugin.openGSpace().then((value) {
          Fluttertoast.showToast(msg: value ? 'jump success' : 'jump failed');
          // 如果打开失败关闭加载状态
          if (!value) changeJumpStatus();
        });
      },
      child: widget.placementBuilder.call(context),
    );
  }

  void changeJumpStatus() => setState(() => jumping = !jumping);

  Future<dynamic> channelCallback(MethodCall call) async {
    Fluttertoast.showToast(msg: '${call.method} params: ${call.arguments}');
    switch (call.method) {
      case 'onInteractiveAdsClose':
      case 'onGSpaceClose':
        changeJumpStatus();
        break;
      case 'returnRewardRecords':
        await widget.handleGSpaceRewards?.call(call.arguments);
        // 发放奖励完成后通知OKSpin
        OkSpinPlugin.notifyGSPubTaskPayout(call.arguments);
        break;
    }
  }
}
