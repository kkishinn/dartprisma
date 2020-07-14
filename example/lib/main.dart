import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_peluhttp/Response.dart';
import 'package:flutter_peluhttp/flutter_peluhttp.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    Response response;
    String payload;
    payload = "{" +
        "        \"card_number\": \"4111972001951111\"," +
        "        \"card_expiration_month\": \"05\"," +
        "        \"card_expiration_year\": \"27\"," +
        "        \"security_code\": \"123\"," +
        "        \"card_holder_name\": \"Oscar Lopez\"," +
        "        \"card_holder_identification\": {" +
        "          \"type\": \"dni\"," +
        "          \"number\": \"34809163\"" +
        "}" +
        "}";


    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await FlutterPeluhttp.platformVersion;
      response = await FlutterPeluhttp.post("https://live.decidir.com/api/v2/tokens", "5236f1914dd74ac5a22bdc6902ca80c2", payload);
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }
}
