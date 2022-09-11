import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:okspin/widget/okspin_placement.dart';
import 'package:okspin/plugin/okspin_plugin.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'OkSpin Demo',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatelessWidget {
  const MyHomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<bool>(
      future: OkSpinPlugin.initSDK(),
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.done) {
          Fluttertoast.showToast(
              msg: snapshot.data ?? false ? 'initSDK success' : 'initSDK fail');
          return const Scaffold(
            body: Center(
              child: Text(
                '点击悬浮按钮进入GSpace',
              ),
            ),
            floatingActionButton: FloatingActionButton(
              onPressed: null,
              child: Center(child: OkSpinPlacementWidget()),
            ), // This trailing comma makes auto-formatting nicer for build methods.
          );
        } else {
          return const Center(child: CircularProgressIndicator());
        }
      },
    );
  }
}
