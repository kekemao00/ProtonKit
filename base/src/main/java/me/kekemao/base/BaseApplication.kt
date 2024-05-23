package me.kekemao.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import me.kekemao.base.utils.ProcessUtils
import kotlin.system.measureTimeMillis


/**
 *  Application 基类
 *
 * @author kekemao - 创建于 2023/4/24 15:05
 */
open class BaseApplication : Application() {

    private val mCoroutineScope by lazy(mode = LazyThreadSafetyMode.NONE) { MainScope() }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var appContext: Context
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        // 全局 context
        appContext = base
    }

    override fun onCreate() {
        super.onCreate()
        initDepend()
    }

    /**
     * 初始化第三方依赖
     *
     */
    private fun initDepend() {
        // 开启一个 Default Coroutine 进行初始化不会立即使用的第三方
        mCoroutineScope.launch(Dispatchers.Default) {
            initByBackstage()
        }
        // 前台初始化

        val allTimeMillis = measureTimeMillis {
            val depends = initByFrontDesk()
            var dependInfo: String
            depends.forEach {
                val dependTimeMillis = measureTimeMillis { dependInfo = it() }
                Log.d("BaseApplication", "initDepend: $dependInfo : $dependTimeMillis ms")
            }
        }
        Log.d("BaseApplication", "初始化完成：$allTimeMillis ms")
    }

    override fun onTerminate() {
        super.onTerminate()
        mCoroutineScope.cancel()
    }

    /**
     * 主线程前台初始化
     * @return MutableList<() -> String> 初始化方法集合
     */
    private fun initByFrontDesk(): MutableList<() -> String> {
        val list = mutableListOf<() -> String>()

        // 以下只需要在主进程当中初始化 按需要调整
        if (ProcessUtils.isMainProcess(appContext)) {
//            list.add {}
        }
        return list
    }

    /**
     * 不需要立即初始化的放在这里进行后台初始化
     *
     */
    private fun initByBackstage() {
//        initTimber2()
    }

}