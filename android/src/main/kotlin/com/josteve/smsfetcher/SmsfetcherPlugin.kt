package com.josteve.smsfetcher

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.annotation.NonNull;
import io.flutter.app.FlutterActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.util.*

/** SmsfetcherPlugin */
public class SmsfetcherPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  var context: Context? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "smsfetcher")
    context = flutterPluginBinding.applicationContext
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
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "smsfetcher")
      channel.setMethodCallHandler(SmsfetcherPlugin())
    }
  }

  private fun getSms(): StringBuffer? {
    val sb = StringBuffer()
    sb.append("[")

    val uriSMSURI = Uri.parse("content://sms")
    val cursor = context!!.contentResolver.query(uriSMSURI, null, null, null, null)

    while (cursor != null && cursor.moveToNext()) {
      val address = cursor.getString(cursor.getColumnIndex("address"))
      val body = cursor.getString(cursor.getColumnIndexOrThrow("body"))
      val date = Date(cursor.getString(cursor.getColumnIndexOrThrow("date")).toLong())
      val type = when {
        cursor.getString(cursor.getColumnIndexOrThrow("type")).contains("1") -> {
          "inbox"
        }
        else -> {
          "sent"
        }
      }
      sb.append("\t{\n\t\"Phone Number\":\"${address.trim()}\",")
      sb.append("\n\t\t\"Message\":\"${body.trim()}\",")
      sb.append("\n\t\t\"Date\":\"${date.toString().trim()}\",")
      sb.append("\n\t\t\"Type\":\"${type.trim()}\"")
      if (cursor.isLast) {
        sb.append("\t}");
      } else {
        sb.append("\t},");
      }
    }
    sb.append("]")
    cursor?.close()

    return sb
  }


  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getSms") {
      result.success(getSms().toString())
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
