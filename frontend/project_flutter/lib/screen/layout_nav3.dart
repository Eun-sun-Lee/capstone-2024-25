import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';

class LayoutNav3 extends StatefulWidget {
  const LayoutNav3({super.key});

  @override
  State<LayoutNav3> createState() {
    return _LayoutNav3State();
  }
}

class _LayoutNav3State extends State<LayoutNav3> {
  final GlobalKey webViewKey = GlobalKey();

  InAppWebViewController? webViewController;

  @override
  Widget build(BuildContext context) {
    return PopScope(
      child: Scaffold(
          body: Column(children: <Widget>[
        Expanded(
          child: InAppWebView(
            key: webViewKey,
            initialUrlRequest: URLRequest(
                url: WebUri("https://allbome-for-vercel.vercel.app/chatbot")),
            onPermissionRequest: (controller, request) async {
              return PermissionResponse(
                  resources: request.resources,
                  action: PermissionResponseAction.GRANT);
            },
            initialSettings: InAppWebViewSettings(
              javaScriptCanOpenWindowsAutomatically: true,
              useShouldOverrideUrlLoading: true,
              useOnLoadResource: true,
              allowsBackForwardNavigationGestures: true,
              javaScriptEnabled: true,
              domStorageEnabled: true,
            ),
            onWebViewCreated: (controller) {
              webViewController = controller;
            },
          ),
        ),
      ])),
    );
  }
}
