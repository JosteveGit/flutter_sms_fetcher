# smsfetcher

# SMS FETCHER
A Flutter Package that makes retrieving of SMS easier from your android phone.
NB: Doesn't support iOS due to Apple's Privacy Concerns. See link below for more info:
[link](https://stackoverflow.com/questions/16187841/read-sms-message-in-ios)

## Widgets
```smsfetcher``` - retrieves a list of sms from your phone

## Example

```import 'package:flutter/material.dart';
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
```
## Contributors
[Josteve Adekanbi](https://github.com/JosteveGit)
[Momoh Herodion](https://github.com/mhero007)
