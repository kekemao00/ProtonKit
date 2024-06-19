package me.kekemao.keyvaluestore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.kekemao.base.BaseApplication

/**
 * Data store manager。
 *
 * 此类事对 DataStore 的封装
 * @since 1
 *
 * @init 初始化：本工具类无需初始化，导入依赖即用： implementation("androidx.datastore:datastore-preferences:1.0.0")
 *
 * @see <a href="https://developer.android.com/topic/libraries/architecture/datastore?hl=zh-cn#kotlin">DataStore</a>
 *
 * @author kekemao - 创建于 2023/5/21 03:43
 *
 * --------------
 *
 * DataStoreManager 工具类的实现看起来很不错，它封装了 DataStore 的常见操作，并使用了 Kotlin 和协程的特性来使代码更加简洁易读。
 *
 * 以下是我对您的代码的一些分析和建议：
 *
 * 1. 类命名及注释：您的 DataStoreManager 工具类命名很好，也提供了很好的注释，非常易于理解。但是，您可以考虑添加更多的注释，以进一步提高代码的可读性和易用性。
 *
 * 2. 单例模式：您的 DataStoreManager 工具类使用了单例模式来确保只有一个实例存在，这样可以避免多个实例之间的冲突和数据不一致。但是，您可以考虑将其改为依赖注入的方式，以更好地支持测试和模块化。
 *
 * 3. 数据类型支持：您的 DataStoreManager 工具类支持了各种数据类型的存储和读取，这是非常好的。但是，您可以考虑进一步扩展它，以支持更多的数据类型（如日期、枚举等），以及自定义的数据类型。
 *
 * 4. 函数命名：您的函数命名很好，很容易理解它们的作用。但是，我建议您将“put”和“get”前缀改为“save”和“retrieve”，这样可以更好地反映它们的功能，并提高代码的可读性和易用性。
 *
 * 5. 异常处理：您的 DataStoreManager 工具类没有对 DataStore 操作中可能出现的异常情况进行处理。我建议您考虑添加错误处理逻辑，以处理 DataStore 操作中可能出现的异常情况，例如当 DataStore 操作失败时应该捕获异常并提供有用的错误信息，以便调用者可以诊断并解决问题。
 *
 * 6. 非空断言：在您的代码中有一些使用了非空断言（如 !!）的地方，这可能导致 NullPointerException 异常。我建议您使用安全调用运算符（如 ?.）或 Elvis 运算符（如 ?:）来避免这种情况的发生。
 *
 * 7. 使用默认值：在您的代码中，当从 DataStore 中读取数据时，您使用了默认值来处理未找到对应键的情况。但是，我建议您考虑将默认值的处理移到更高层次的代码中，以便从 DataStore 中检索数据的代码更加专注于数据检索，并让调用者更容易理解和处理数据的缺失情况。
 *
 * 8. Kotlin 的协程：您使用了 Kotlin 的协程来处理异步操作，这是一个很好的选择。但是，在某些情况下，您可能需要使用 withContext() 函数将代码切换到不同的协程上下文中，以避免在主线程上执行长时间运行的操作。
 *
 *  - by sage
 *-------------------
 *
 *  这段代码是一个对DataStore进行封装的工具类，通过提供put和get方法来简化DataStore的使用。下面是一些建议：
 *
 * 在注释中提供更多的信息：提供更多的注释来解释每个函数的作用、参数和返回值的含义。这将使代码更易于理解和维护。
 *
 * 使用更具描述性的变量名：在有些函数中，变量的名称可能有些模糊。例如，使用key而不是具有更具描述性的名称来表示要保存的数据项名称。使用更具描述性的名称将使代码更易于理解和维护。
 *
 * 优化代码结构：可以将一些重复的代码抽象成一个单独的函数，以避免重复代码。例如，put和get函数的实现方式相同，可以将它们合并为一个通用函数来减少代码重复。
 *
 * 考虑使用Kotlin协程：在这个类中，使用了runBlocking来在主线程中等待DataStore的结果。但是，这是一种阻塞式的操作，可能会影响应用程序的性能。建议使用Kotlin协程来异步地获取DataStore的结果，以避免阻塞主线程并提高应用程序的性能。同时，建议将ioDispatcher变量的类型更改为CoroutineDispatcher，以避免在使用它时出现类型转换错误。
 *
 * 考虑添加异常处理：在putData和getData函数中，如果传递的类型不受支持，将会抛出IllegalArgumentException异常。建议在这些函数中添加异常处理，以更好地处理这种情况。
 *
 * 考虑使用泛型参数：在putData和getData函数中，使用了一个when语句来确定传递的值的类型，并调用相应的put和get函数。建议使用泛型参数来更好地支持不同类型的数据。这样可以使代码更加通用和可重用。
 *
 * 考虑添加数据有效性检查：在putData和getData函数中，没有检查传递的key是否为null或空字符串。建议添加数据有效性检查，以避免传递无效的key值。
 *
 * 考虑添加数据加密：在这个类中，没有提供数据加密的功能。如果存储的数据需要保密性，建议添加数据加密的功能来确保数据的安全性。可以使用Android提供的加密库或第三方加密库来实现数据加密。
 *
 * - by ChatGPT 3.5
 *
 */
object DataStoreManager {

    private val TAG: String = DataStoreManager::class.java.name

    /**
     * 默认 DataStore 名称
     */
    private const val DEFAULT_PREFERENCES_NAME = "default_preferences_name"

    /**
     * 获取 DataStore 对象
     */
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = DEFAULT_PREFERENCES_NAME
    )

    /**
     * 获取 DataStore 对象
     */
    private val dataStore = BaseApplication.appContext.dataStore

    /**
     * IO 线程调度器
     */
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    /**
     * 保存 String 类型的数据
     *
     * @param key
     * @param value
     */
    private suspend fun saveString(key: String, value: String) =
        withContext(ioDispatcher) {
            dataStore.edit { it[stringPreferencesKey(key)] = value }
        }

    /**
     * 保存 String Set 类型的数据
     *
     * @param key
     * @param value
     */
    private suspend fun saveStringSet(key: String, value: Set<String>) =
        withContext(ioDispatcher) {
            dataStore.edit { it[stringSetPreferencesKey(key)] = value }
        }

    /**
     * 保存 Int 类型的数据
     *
     * @param key
     * @param value
     */
    private suspend fun saveInt(key: String, value: Int) =
        withContext(ioDispatcher) {
            dataStore.edit { it[intPreferencesKey(key)] = value }
        }

    /**
     * 保存 Long 类型的数据
     *
     * @param key
     * @param value
     */
    private suspend fun saveLong(key: String, value: Long) =
        withContext(ioDispatcher) {
            dataStore.edit { it[longPreferencesKey(key)] = value }
        }

    /**
     * 保存 Float 类型的数据
     *
     * @param key
     * @param value
     */
    private suspend fun saveFloat(key: String, value: Float) =
        withContext(ioDispatcher) {
            dataStore.edit { it[floatPreferencesKey(key)] = value }
        }

    /**
     * 保存 Double 类型的数据
     *
     * @param key
     * @param value
     */
    private suspend fun saveDouble(key: String, value: Double) =
        withContext(ioDispatcher) {
            dataStore.edit { it[doublePreferencesKey(key)] = value }
        }

    /**
     * 保存 Boolean 类型的数据
     *
     * @param key
     * @param value
     */
    private suspend fun saveBoolean(key: String, value: Boolean) =
        withContext(ioDispatcher) {
            dataStore.edit { it[booleanPreferencesKey(key)] = value }
        }

    /**
     * 获取 String 类型的数据
     *
     * @param key
     * @param default
     * @return
     */
    private fun retrieveString(key: String, default: String? = null): String? =
        runBlocking {
            dataStore.data.map { it[stringPreferencesKey(key)] ?: default }.first()
        }

    /**
     * 获取 String Set 类型的数据
     *
     * @param key
     * @param default
     * @return
     */
    private fun retrieveStringSet(key: String, default: Set<String>? = null): Set<String>? =
        runBlocking {
            dataStore.data.map { it[stringSetPreferencesKey(key)] ?: default }.first()
        }

    /**
     * 获取 Int 类型的数据
     *
     * @param key
     * @param default
     * @return
     */
    private fun retrieveInt(key: String, default: Int = 0): Int =
        runBlocking {
            dataStore.data.map { it[intPreferencesKey(key)] ?: default }.first()
        }

    /**
     * 获取 Long 类型的数据
     *
     * @param key
     * @param default
     * @return
     */
    private fun retrieveLong(key: String, default: Long = 0L): Long =
        runBlocking {
            dataStore.data.map { it[longPreferencesKey(key)] ?: default }.first()
        }

    /**
     * 获取 Float 类型的数据
     *
     * @param key
     * @param default
     * @return
     */
    private fun retrieveFloat(key: String, default: Float = 0F): Float =
        runBlocking {
            dataStore.data.map { it[floatPreferencesKey(key)] ?: default }.first()
        }

    /**
     * 获取 Double 类型的数据
     *
     * @param key
     * @param default
     * @return
     */
    private fun retrieveDouble(key: String, default: Double = 0.0): Double =
        runBlocking {
            dataStore.data.map { it[doublePreferencesKey(key)] ?: default }.first()
        }

    /**
     * 获取 Boolean 类型的数据
     *
     * @param key
     * @param default
     * @return
     */
    private fun retrieveBoolean(key: String, default: Boolean = false): Boolean =
        runBlocking {
            dataStore.data.map {
                it[booleanPreferencesKey(key)] ?: default
            }.first()
        }

    /**
     * 保存任意类型的数据
     *
     * @param T
     * @param key
     * @param value
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun <T> save(key: String, value: T) {
        Log.d(TAG, "save: $key=$value\"")
        when (value) {
            is String -> saveString(key, value)
            is Set<*> -> saveStringSet(key, value as Set<String>)
            is Int -> saveInt(key, value)
            is Long -> saveLong(key, value)
            is Float -> saveFloat(key, value)
            is Double -> saveDouble(key, value)
            is Boolean -> saveBoolean(key, value)
            else -> throw IllegalArgumentException("This type cannot be saved to the Data Store")
        }
    }

    /**
     * 获取任意类型的数据
     *
     * @param T
     * @param key
     * @param default
     * @return
     */
    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    fun <T> retrieve(key: String, default: T): T {

        val result = when (default) {
            is String -> retrieveString(key, default)
            is Set<*> -> retrieveStringSet(key, default as Set<String>)
            is Int -> retrieveInt(key, default)
            is Long -> retrieveLong(key, default)
            is Float -> retrieveFloat(key, default)
            is Double -> retrieveDouble(key, default)
            is Boolean -> retrieveBoolean(key, default)
            else -> throw IllegalArgumentException("This type cannot be saved to the Data Store")
        } as T

        Log.d(TAG, "retrieve: $key=$result")

        return result
    }

    /**
     * 清空所有数据
     */
    suspend fun clearData() =
        withContext(ioDispatcher) {
            dataStore.edit { it.clear() }
        }
}