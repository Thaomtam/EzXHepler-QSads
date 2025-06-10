package com.KTA.QSads

import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.Log.logexIfThrow
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == "com.android.chrome") {
            EzXHelperInit.initHandleLoadPackage(lpparam)
            EzXHelperInit.setLogTag("AdBlock")
            EzXHelperInit.setToastTag("AdBlock")

            initHooks(
                AdBlockNetworkHook,
                AdBlockCosmeticHook,
                AdBlockBypassHook
            )
        }
    }

    private fun initHooks(vararg hook: BaseHook) {
        hook.forEach {
            runCatching {
                if (it.isInit) return@forEach
                it.init()
                it.isInit = true
                Log.i("Inited hook: ${it.javaClass.simpleName}")
            }.logexIfThrow("Failed to init hook")
        }
    }
}