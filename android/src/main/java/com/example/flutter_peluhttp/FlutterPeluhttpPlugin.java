package com.example.flutter_peluhttp;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterPeluhttpPlugin */
public class FlutterPeluhttpPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_peluhttp");
    channel.setMethodCallHandler(this);
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_peluhttp");
    channel.setMethodCallHandler(new FlutterPeluhttpPlugin());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if(call.method.equals("post")){
      String url = call.argument("url").toString();
      String apikey = call.argument("apikey").toString();
      String payload = call.argument("payload").toString();
      //String response = postData(url, apikey, payload);
      Response response = null;

      try {
        response = new postAsync(url, apikey, payload).execute().get();
      } catch (ExecutionException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      result.success(response.toJSON());
    }else {
      result.notImplemented();
    }
  }

  public class postAsync extends AsyncTask<Void, Void, Response> {
      private String url;
      private String apikey;
      private String payload;

      public postAsync(String url, String apikey, String payload){
        this.url = url;
        this.apikey = apikey;
        this.payload = payload;
      }

    @Override
    protected Response doInBackground(Void... params) {
      return postData(url, apikey, payload);
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  private Response postData(String url, String apikey, String payload){
    int statusCode = 0;
    String response = "";
    BufferedReader reader = null;

    try {
      URL endPoint = new URL(url);
      HttpURLConnection conn = (HttpURLConnection)endPoint.openConnection();;
      conn.setRequestProperty("Content-Type", "application/json"); //Esto esta hardcodeado, tratar de cambiar despues para hacerlo dinamico
      conn.setRequestProperty("apikey", apikey); //Esto esta hardcodeado, tratar de cambiar despues para hacerlo dinamico
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setRequestMethod("POST");

      DataOutputStream localDataOutputStream = new DataOutputStream(conn.getOutputStream());
      localDataOutputStream.writeBytes(payload);
      localDataOutputStream.flush();
      localDataOutputStream.close();

      statusCode = conn.getResponseCode();


      if(conn.getErrorStream() == null){
        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      }else{
        reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
      }

        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
          sb.append(line + "\n");
        }
        response = sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new Response(statusCode, response);
  }
  }
