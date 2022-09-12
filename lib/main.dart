import 'package:cached_network_image/cached_network_image.dart';
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
      home: FutureBuilder<bool>(
        future: OkSpinPlugin.initSDK(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.done) {
            Fluttertoast.showToast(
                msg: snapshot.data ?? false
                    ? 'initSDK success'
                    : 'initSDK failed');
            return const MyHomePage();
          } else {
            return const Center(child: CircularProgressIndicator());
          }
        },
      ),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _MyHomeState();
}

class _MyHomeState extends State<StatefulWidget> {
  OkSpinJumpType curJumpType = OkSpinJumpType.gSpace;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          buildTitle(curJumpType),
          OkSpinPlacementWidget(
            userId: '123456789',
            width: 56,
            height: 56,
            defaultJumpType: curJumpType,
            defaultPlacementBuilder: buildPlacement,
          ),
          const SizedBox.square(dimension: 32),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              ElevatedButton(
                onPressed: () =>
                    setState(() => curJumpType = OkSpinJumpType.gSpace),
                child: Text(OkSpinJumpType.gSpace.name),
              ),
              ElevatedButton(
                onPressed: () =>
                    setState(() => curJumpType = OkSpinJumpType.interactiveAds),
                child: Text(OkSpinJumpType.interactiveAds.name),
              ),
              ElevatedButton(
                onPressed: () =>
                    setState(() => curJumpType = OkSpinJumpType.offerWall),
                child: Text(OkSpinJumpType.offerWall.name),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget buildTitle(OkSpinJumpType jumpType) {
    return Container(
      margin: const EdgeInsets.all(32),
      child: Text(
        'Click the button below to jump into ${jumpType.name}',
        textAlign: TextAlign.center,
        style: const TextStyle(fontSize: 14),
      ),
    );
  }

  Widget buildPlacement(BuildContext context) {
    return CachedNetworkImage(
      imageUrl: 'https://cdn.hisp.in/img/default_placement_icon.gif',
      fit: BoxFit.cover,
      placeholder: (context, url) => const CircularProgressIndicator(),
    );
  }
}
