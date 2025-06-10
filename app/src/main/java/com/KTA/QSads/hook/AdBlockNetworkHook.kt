package com.KTA.QSads.hook

import android.webkit.*
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.XC_MethodHook

object AdBlockNetworkHook : BaseHook() {
    private val adDomains = listOf(
        "pagead2.googlesyndication.com",
        "adservice.google.com",
        "doubleclick.net",
        "taboola.com"
    )

    override fun init() {
        findMethod("android.webkit.WebViewClient") {
            name == "shouldInterceptRequest" &&
            parameterCount >= 2 &&
            parameterTypes.contains(WebView::class.java) &&
            parameterTypes.contains(WebResourceRequest::class.java)
        }.hookAfter { param ->
            val webView = param.args[0] as? WebView ?: return@hookAfter
            val request = param.args[1] as? WebResourceRequest ?: return@hookAfter
            val url = request.url.toString()

            for (domain in adDomains) {
                if (url.contains(domain)) {
                    param.result = WebResourceResponse(null, null, null)
                    Log.i(TAG, "Blocked network request to: $url")
                    return@hookAfter
                }
            }
        }
    }
}