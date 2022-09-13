import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:okspin/plugin/okspin_plugin.dart';

class OkSpinPlacementWidget extends StatefulWidget {
  const OkSpinPlacementWidget(
      {Key? key,
      required this.userId,
      this.defaultJumpType = OkSpinJumpType.gSpace,
      required this.defaultPlacementBuilder,
      this.handleGSpaceRewards,
      this.handleInteractiveAdsTotalReward,
      this.handleOfferWallTotalReward,
      required this.width,
      required this.height})
      : super(key: key);

  final String userId;
  final double width;
  final double height;
  final OkSpinJumpType defaultJumpType;
  final WidgetBuilder defaultPlacementBuilder;
  final Future<void> Function(List<String> rewardIds)? handleGSpaceRewards;
  final void Function(int totalReward)? handleInteractiveAdsTotalReward;
  final void Function(int totalReward)? handleOfferWallTotalReward;

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
    return SizedBox(
      width: widget.width,
      height: widget.height,
      child: Center(
        child: jumping ? const CircularProgressIndicator() : buildPlacement(),
      ),
    );
  }

  Widget buildPlacement() {
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
                  "width": widget.width.toInt(),
                  "height": widget.height.toInt(),
                },
                creationParamsCodec: const StandardMessageCodec(),
              ),
            );
          } else {
            return buildDefaultPlacement();
          }
        } else {
          return const CircularProgressIndicator();
        }
      },
    );
  }

  Widget buildDefaultPlacement() {
    return InkWell(
      radius: 0,
      highlightColor: Colors.transparent,
      onTap: () {
        changeJumpStatus();
        jumpToOkSpin();
      },
      child: widget.defaultPlacementBuilder.call(context),
    );
  }

  void changeJumpStatus() => setState(() => jumping = !jumping);

  Future<dynamic> channelCallback(MethodCall call) async {
    Fluttertoast.showToast(msg: '${call.method} params: ${call.arguments}');
    switch (call.method) {
      case 'onGSpaceClose':
        changeJumpStatus();
        break;
      case 'returnGSpaceRewardIds':
        await widget.handleGSpaceRewards?.call(call.arguments);
        // 发放GSpace实物奖励完成后通知OKSpin
        OkSpinPlugin.notifyGSPubTaskPayout();
        break;
      case 'returnInteractiveAdsTotalReward':
        widget.handleInteractiveAdsTotalReward?.call(call.arguments);
        break;
      case 'returnOfferWallTotalReward':
        widget.handleOfferWallTotalReward?.call(call.arguments);
        break;
    }
  }

  void jumpToOkSpin() {
    Future<bool> jumpFuture;
    switch (widget.defaultJumpType) {
      case OkSpinJumpType.gSpace:
        jumpFuture = OkSpinPlugin.openGSpace();
        break;
      case OkSpinJumpType.interactiveAds:
        jumpFuture = OkSpinPlugin.openInteractiveAds();
        break;
      case OkSpinJumpType.offerWall:
        jumpFuture = OkSpinPlugin.openOfferWall();
        break;
    }
    jumpFuture.then((value) {
      Fluttertoast.showToast(msg: value ? 'jump success' : 'jump failed');
      // 如果跳转失败也关闭加载状态
      if (!value) changeJumpStatus();
    });
  }
}

enum OkSpinJumpType { gSpace, interactiveAds, offerWall }
