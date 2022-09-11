import 'package:flutter/material.dart';

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
    return const Scaffold(
      body: Center(
        child: Text(
          '点击悬浮按钮进入GSpace',
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: null,
        child: AndroidView(viewType: 'OkSpinEntryView'),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
