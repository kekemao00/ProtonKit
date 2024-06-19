package me.kekemao.keyvaluestore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * Data store manager。
 *
 * 此类事对 DataStore 的封装
 * @since 1
 *
 * @init 初始化：本工具类无需初始化，导入依赖即用： implementation("androidx.datastore:datastore-preferences:1.0.0")
 *
 */
data class DataStoreManager(
    private val context: Context, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        /**
         * 消息 KEY
         */
        const val SP_KEY_MESSAGE: String = "SP_KEY_MESSAGE"

    }

    private val TAG: String = DataStoreManager::class.java.name

    /**
     * 默认 DataStore 名称
     */
    private val DEFAULT_PREFERENCES_NAME = "default_preferences_name"

    /**
     * 获取 DataStore 对象
     */
    private val Context.dataStore by preferencesDataStore(name = DEFAULT_PREFERENCES_NAME)

    /**
     * 获取 DataStore 对象
     */
    private val dataStore get() = context.dataStore


    /**
     * 保存数据
     *
     * @param key 保存的 key  @see [SP_KEY_MESSAGE]
     * @param value 保存的数据 (String, Set<String>, Int, Long, Float, Double, Boolean)
     */
    suspend fun <T : Any> save(key: String, value: T) {
        Log.v(TAG, "save: Storing locally: $key=$value")
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                when (value) {
                    is String -> preferences[stringPreferencesKey(key)] = value
                    is Set<*> -> preferences[stringSetPreferencesKey(key)] = value as Set<String>
                    is Int -> preferences[intPreferencesKey(key)] = value
                    is Long -> preferences[longPreferencesKey(key)] = value
                    is Float -> preferences[floatPreferencesKey(key)] = value
                    is Double -> preferences[doublePreferencesKey(key)] = value
                    is Boolean -> preferences[booleanPreferencesKey(key)] = value
                    else -> throw IllegalArgumentException("Unsupported type")
                }
            }
        }
    }

    /**
     * 读取数据
     *
     * @param key 读取的 key  @see [SP_KEY_MESSAGE]
     * @param defaultValue 默认值 (String, Set<String>, Int, Long, Float, Double, Boolean)
     */
    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    fun <T : Any> retrieve(key: String, defaultValue: T): T {
        Log.v(TAG, "retrieve: Reading local cache: $key")
        return runBlocking {
            dataStore.data.catch {
                Log.w(TAG, it)
                emit(emptyPreferences())
            }.map { preferences ->
                when (defaultValue) {
                    is String -> preferences[stringPreferencesKey(key)] ?: defaultValue
                    is Set<*> -> preferences[stringSetPreferencesKey(key)] ?: defaultValue
                    is Int -> preferences[intPreferencesKey(key)] ?: defaultValue
                    is Long -> preferences[longPreferencesKey(key)] ?: defaultValue
                    is Float -> preferences[floatPreferencesKey(key)] ?: defaultValue
                    is Double -> preferences[doublePreferencesKey(key)] ?: defaultValue
                    is Boolean -> preferences[booleanPreferencesKey(key)] ?: defaultValue
                    else -> throw IllegalArgumentException("Unsupported type")
                } as T
            }.first()
        }
    }

    /**
     * 清空所有数据
     */
    suspend fun clearData() = withContext(ioDispatcher) {
        dataStore.edit { it.clear() }
    }
}