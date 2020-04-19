import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';
import 'package:permission/permission.dart';

class Smsfetcher {
  static const MethodChannel _channel = const MethodChannel('smsfetcher');

  static Future<List<Sms>> get _getSms async {
    final String response = await _channel.invokeMethod('getSms');
    print(response);
    return parseSms(response);
  }

  static Future<List<Sms>> getSms() async{
    var permissionList =
        await Permission.getPermissionsStatus([PermissionName.SMS,  PermissionName.Contacts]);
    if (permissionList.first.permissionStatus == PermissionStatus.allow) {
      return _getSms;
    } else {
      var request = await Permission.requestPermissions([PermissionName.SMS, PermissionName.Contacts]);
      if (request.first.permissionStatus == PermissionStatus.allow) {
        return _getSms;
      } else {
        throw new Exception("Unable to fetch sms. Permission denied");
      }
    }
  }
}

List<Sms> parseSms(String responseBody) {
  final parsed = json.decode(responseBody).cast<Map<String, dynamic>>();
  List<Sms> messages = [];
  parsed.forEach((element) => messages.add(Sms.fromMap(element as Map)));
  return messages;
}

class Sms {
  final String phoneNumber;
  final String message;
  final String type;
  final String dateTime;

  Sms(this.phoneNumber, this.message, this.type, this.dateTime);

  factory Sms.fromMap(Map<String, dynamic> map) {
    return Sms(map["Phone Number"], map["Message"], map["Type"], map["Date"]);
  }

  String toString() {
    return "Sms(phoneNumber: $phoneNumber, message: $message, type: $type, dateTime: $dateTime)";
  }
}
