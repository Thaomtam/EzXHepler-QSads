package com.KTA.QSads

import com.KTA.QSads.hook.BaseHook
import com.KTA.QSads.hook.AdBlockBypassHook
import com.KTA.QSads.hook.AdBlockCosmeticHook
import com.KTA.QSads.hook.AdBlockNetworkHook
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.Log.logexIfThrow
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

private const val TAG = "QSads"

class MainHook : IXposedHookLoadPackage, IXposedHookZygoteInit {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        // Khởi tạo EzXHelper
        EzXHelperInit.initHandleLoadPackage(lpparam)
        EzXHelperInit.setLogTag(AdBlock)
        EzXHelperInit.setToastTag(AdBlock)

        // Không cần kiểm tra package cụ thể → để LSPosed kiểm soát qua scope
        initHooks(
                AdBlockNetworkHook,
                AdBlockCosmeticHook,
                AdBlockBypassHook
            )
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        EzXHelperInit.initZygote(startupParam)
    }

    private fun initHooks(vararg hook: BaseHook) {
        hook.forEach {
            runCatching {
                if (it.isInit) return@forEach
                it.init()
                it.isInit = true
                Log.i("Inited hook: ${it.javaClass.simpleName}")
            }.logexIfThrow("Failed init hook: ${it.javaClass.simpleName}")
        }
    }
}
