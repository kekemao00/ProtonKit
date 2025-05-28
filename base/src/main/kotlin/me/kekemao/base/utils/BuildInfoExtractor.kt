import android.os.Build
import java.lang.reflect.Field

fun main() {
    val buildInfo = getBuildInfo()
    buildInfo.forEach { (key, value) ->
            println("$key : $value")
    }
}

/**
 * Get build info
 *
 * eg:
 * ```
 *       val buildInfo = getBuildInfo()
 *         for ((key, value) in buildInfo) {
 *             println("$key : $value")
 *         }
 * ```
 *
 * @return
 */
fun getBuildInfo(): Map<String, String> {
    val buildInfoMap = mutableMapOf<String, String>()

    // 获取 Build 类的所有字段
    val fields: Array<Field> = Build::class.java.declaredFields
    for (field in fields) {
        try {
            // 获取字段名
            val name = field.name
            // 获取字段的值
            val value = field.get(null)
            // 将字段名和值添加到 Map 中
            buildInfoMap[name] = value?.toString() ?: "null"
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    return buildInfoMap
}
