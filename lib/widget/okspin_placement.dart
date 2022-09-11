import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:okspin/plugin/okspin_plugin.dart';

class OkSpinPlacementWidget extends StatefulWidget {
  const OkSpinPlacementWidget({Key? key, required this.placementBuilder})
      : super(key: key);

  final WidgetBuilder placementBuilder;

  @override
  State<StatefulWidget> createState() => _OkSpinPlacementState();
}

class _OkSpinPlacementState extends State<OkSpinPlacementWidget> {
  bool spaceOpening = false;
  Future<bool>? getPlacementFuture;

  @override
  void initState() {
    super.initState();
    OkSpinPlugin.channel.setMethodCallHandler(channelCallback);
  }

  @override
  void dispose() {
    OkSpinPlugin.channel.setMethodCallHandler(null);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: spaceOpening
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
                onTap: changeStatus,
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
        changeStatus();
        OkSpinPlugin.openGSpace();
      },
      child: widget.placementBuilder.call(context),
    );
  }

  void changeStatus() => setState(() => spaceOpening = !spaceOpening);

  Future<dynamic> channelCallback(MethodCall call) async {
    switch (call.method) {
      case 'onGSpaceClose':
        changeStatus();
        break;
    }
  }
}
