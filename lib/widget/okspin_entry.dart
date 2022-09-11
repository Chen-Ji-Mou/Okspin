import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:okspin/plugin/okspin_plugin.dart';

class OkSpinEntryView extends StatefulWidget {
  const OkSpinEntryView({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _OkSpinEntryState();
}

class _OkSpinEntryState extends State<OkSpinEntryView> {

  @override
  void initState() {
    super.initState();
    initEntryView();
    OkSpinPlugin.channel.setMethodCallHandler(_methodCallback);
  }

  Future<void> initEntryView() async {
    final dynamic result = await OkSpinPlugin.getEntryView();
    if (result is String && mounted) {
      setState(() {
        Fluttertoast.showToast(msg: result);
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return const AndroidView(viewType: 'plugins.flutter.io/okspin_entry_view');
  }

  Future<dynamic> _methodCallback(MethodCall call) async {}
}
