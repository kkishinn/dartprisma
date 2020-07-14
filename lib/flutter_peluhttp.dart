import 'dart:async';
import 'dart:convert';
import 'dart:io' show Platform;
import 'package:http/http.dart' as http;
import 'package:flutter/services.dart';

import 'Response.dart';

class FlutterPeluhttp {
  static const MethodChannel _channel =
      const MethodChannel('flutter_peluhttp');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<Response> post(String url, String apiKey, String payload) async{
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("url", () => url);
    args.putIfAbsent("apikey", () => apiKey);
    args.putIfAbsent("payload", () => payload);
    Response response = new Response(-1, "");

    if(Platform.isAndroid){ //Si es Android usa las funciones internas de Android para hacer la request
      Map<String, dynamic> result = jsonDecode(await _channel.invokeMethod('post', args));
      response = new Response(result["statusCode"], result["response"]);
    }else{ //Si es otro SO, usa la libreria HTTP de flutter (Suponiendo que el problema sea solo Android)
      final http.Response generalResponse = await http.post(url,
          body: payload,
          headers: {
            "apikey": apiKey,
            "Content-Type": "application/json",
            "Cache-Control": "no-cache"
          }
      );

     response = new Response(generalResponse.statusCode, generalResponse.body);
    }

    return response;
  }
}
