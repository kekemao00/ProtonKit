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
    private fun retrieveInt(key: String, default: Int? = null): Int? =
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
    private fun retrieveLong(key: String, default: Long? = null): Long? =
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
    private fun retrieveFloat(key: String, default: Float? = null): Float? =
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
    private fun retrieveDouble(key: String, default: Double? = null): Double? =
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
    private fun retrieveBoolean(key: String, default: Boolean? = null): Boolean? =
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