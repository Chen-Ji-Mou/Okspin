import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:okspin/plugin/okspin_plugin.dart';

class OkSpinPlacementWidget extends StatefulWidget {
  const OkSpinPlacementWidget({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _OkSpinPlacementState();
}

class _OkSpinPlacementState extends State<OkSpinPlacementWidget> {
  @override
  void initState() {
    super.initState();
    OkSpinPlugin.getPlacement().then((value) {
      Fluttertoast.showToast(
          msg: value ? 'getPlacement success' : 'getPlacement fail');
    });
    OkSpinPlugin.channel.setMethodCallHandler(_methodCallback);
  }

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(builder: (context, constraints) {
      return AndroidView(
        viewType: 'plugins.flutter.io/okspin_placement',
        creationParams: <String, int>{
          "width": constraints.maxWidth.toInt() ~/ 2,
          "height": constraints.maxHeight.toInt() ~/ 2,
        },
        creationParamsCodec: const StandardMessageCodec(),
      );
    });
  }

  Future<dynamic> _methodCallback(MethodCall call) async {}
}
