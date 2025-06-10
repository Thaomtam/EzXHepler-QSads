package com.KTA.QSads.hook

import android.webkit.*
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.XC_MethodHook

object AdBlockBypassHook : BaseHook() {
    override fun init() {
        findMethod("android.webkit.WebViewClient") {
            name == "onPageFinished" && parameterCount == 2
        }.hookAfter { param ->
            val webView = param.args[0] as? WebView ?: return@hookAfter
            val bypassScript = """
                (function () {
                    window.googletag = {};
                    window.adsbygoogle = [];

                    Object.defineProperty(window, 'adBlockEnabled', {
                        value: false,
                        writable: false
                    });

                    Object.defineProperty(window, 'canRunAds', {
                        value: true,
                        writable: false
                    });
                })()
            """.trimIndent()
            webView.evaluateJavascript(bypassScript, null)
            Log.i("AdBlock", "Injected anti-adblock bypass script.")
        }
    }
}