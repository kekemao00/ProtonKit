package me.kekemao.base.utils

import android.content.ContextWrapper
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.R
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Set default uncaught exception handler
 * 设置默认的未捕获异常处理程序
 *
 * 1. 记录未捕获的异常日志，并准备重启应用程序
 * 1. 鼓励反馈：在文案中加入了鼓励反馈的提示，向用户传达他们的反馈对改进应用很重要。
 * 2. 反馈渠道：代码中添加了通过邮件发送反馈的逻辑，用户可以直接通过邮件向开发团队报告问题。可以根据需求定制其他反馈方式，比如跳转到应用的反馈页面、通过在线表单等。
 * 3. 简化用户操作：通过自动填充邮件内容（如主题、正文模板），让用户能够轻松反馈。
 *
 */
private fun setDefaultUncaughtExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
        // 记录未捕获的异常日志，并准备重启应用程序
        Timber.e(exception, "Uncaught exception in thread: $thread")

        // 记录崩溃发生的时间
        val crashTime =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        // 持久化异常日志，保存到文件中
        saveCrashLogToFile(thread, exception, crashTime)

        // 判断是否在主线程，以决定如何显示提示
        if (Looper.myLooper() == Looper.getMainLooper()) {
            try {
                // 提示用户应用即将重启并鼓励反馈问题
                Toast.makeText(
                    this,
                    getString(R.string.string_uncaught_exception_tips),
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                // 在后台可能无法显示Toast，捕获异常
                Timber.e(e, "Unable to show Toast message")
            }
        }

        // 提前执行一些必要的清理工作
        // TODO: 添加必要的清理任务，例如关闭资源

        // 组装错误报告，包括堆栈信息、设备信息等
        val stackTrace = Log.getStackTraceString(exception) // 获取详细堆栈信息
        val feedbackIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:ruanjian002@soundpeatsaudio.com")
            addFlags(FLAG_ACTIVITY_NEW_TASK)
            putExtra(
                Intent.EXTRA_SUBJECT,
                "SOUNDPEATS【应用异常反馈】"
            )
            val deviceName =
                SPUtils.getInstance().getString(FwTypeConstant.SP_KEY_BLUETOOTH_NAME, "-")
            val deviceFwCode =
                SPUtils.getInstance().getString(FwTypeConstant.SP_KEY_FW_TYPE, "-")
            val deviceVersion =
                SPUtils.getInstance().getString(FwTypeConstant.SP_KEY_CURRENT_VERSION, "-")
            putExtra(
                Intent.EXTRA_TEXT,
                // TODO 文案国际化
                "亲爱的开发团队，您好！\n\n" +
                        "我在使用应用时遇到了一个问题，以下是我的设备及系统环境信息：\n" +
                        "App版本号：${BuildConfig.VERSION_NAME}" +
                        "耳机信息：name=$deviceName,fwCode=$deviceFwCode,version=$deviceVersion" +
                        "设备品牌/型号: ${Build.BRAND} / ${Build.MODEL}\n" +
                        "Android版本: ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})\n" +
                        "应用版本: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})\n" +
                        "分辨率: ${resources.displayMetrics.widthPixels}x${resources.displayMetrics.heightPixels}\n" +
                        "CPU架构: ${Build.SUPPORTED_ABIS.joinToString()}\n" +
                        "当前语言: ${Locale.getDefault().language}-${Locale.getDefault().country}\n" +
                        "崩溃时间: $crashTime\n" +
                        "线程信息: ${thread.name}\n" +
                        "错误描述: ${exception.message}\n" +
                        "堆栈信息:\n$stackTrace\n\n" +
                        "问题描述：\n（请在此详细描述您遇到的问题以及操作步骤）\n"
            )
        }
        if (feedbackIntent.resolveActivity(packageManager) != null) {
            startActivity(feedbackIntent)
        }

        // 重启应用程序
        AppUtils.relaunchApp(true)
    }
}

/**
 * 保存崩溃日志到文件
 */
private fun ContextWrapper.saveCrashLogToFile(thread: Thread, exception: Throwable, crashTime: String) {
    try {
        val logFile = File(filesDir, "crash_log.txt")
        if (!logFile.exists()) {
            logFile.createNewFile()
        }
        val stackTrace = Log.getStackTraceString(exception)
        logFile.appendText(
            "崩溃时间: $crashTime\n" +
                    "线程: ${thread.name}\n" +
                    "错误信息: ${exception.message}\n" +
                    "堆栈信息:\n$stackTrace\n\n"
        )
    } catch (e: IOException) {
        Timber.e(e, "Unable to save crash log to file")
    }
}
