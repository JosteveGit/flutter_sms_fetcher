
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:smsfetcher/smsfetcher.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<Sms> _smsList;


  Future<void> fetchSms() async {
    List<Sms> smsList;
    try {
      smsList = await Smsfetcher.getSms();
    }on PlatformException{
      print("Error");
    }
    setState(() {
      _smsList = smsList;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Sms Fetcher'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              Text('$_smsList'),
              RaisedButton(
                onPressed: (){
                  fetchSms();
                },
                child: Text("Fetch sms"),
                color: Colors.orange,
              )
            ],
          ),
        ),
      ),
    );
  }
}
