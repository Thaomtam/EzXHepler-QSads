package com.KTA.QSads

import android.webkit.*
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.XC_MethodHook

object AdBlockCosmeticHook : BaseHook() {
    override fun init() {
        findMethod("android.webkit.WebViewClient") {
            name == "onPageFinished" && parameterCount == 2
        }.hookAfter { param ->
            val webView = param.args[0] as? WebView ?: return@hookAfter
            val js = """
                (function () {
                    document.querySelectorAll('.ad, .adsbygoogle, #ad-container, [id*="google_ads"]').forEach(el => el.remove());
                })()
            """.trimIndent()
            webView.evaluateJavascript(js, null)
            Log.i("Removed cosmetic ads from page.") // Fix: Use single message parameter
        }
    }
}