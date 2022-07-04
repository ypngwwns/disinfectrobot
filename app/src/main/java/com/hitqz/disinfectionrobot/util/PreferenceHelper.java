package com.hitqz.disinfectionrobot.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

/** SharedPreferences存储，读取帮助工具类
 * 
 * @author wwl */
public class PreferenceHelper {
	private static final String TAG = "PreferenceHelper";

	/** SharedPreferences编辑 */
	private static Editor mEditor = null;
	/** SharedPreferences存储 */
	private static SharedPreferences mSharedPreferences = null;

	public PreferenceHelper() {
	}

	/** 获取/初始化Editor编辑类实例
	 * 
	 * @param context
	 * @return */
	public static Editor getEditor(Context context) {
		if (mEditor == null)
			mEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		return mEditor;
	}

	/** 获取/初始化SharedPreferences轻量级的存储类实例
	 * 
	 * @param pContext
	 * @return */
	public static SharedPreferences getSharedPreferences(Context pContext) {
		if (mSharedPreferences == null)
			mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(pContext);
		return mSharedPreferences;
	}

	/** 存数据
	 * 
	 * @param key
	 * @param value */
	public static void saveToSharedPreferences(String key, Object value) {
		if ((value instanceof String)) {// 是否是String类
			mEditor.putString(key, (String) value);
			Log.i(TAG, key + "=" + value);
		} else if ((value instanceof Integer)) {// 是否是Integer类
			mEditor.putInt(key, (Integer) value);
			Log.i(TAG, key + "=" + value);
		} else if ((value instanceof Boolean)) {// 是否是Boolean类
			mEditor.putBoolean(key, (Boolean) value);
			Log.i(TAG, key + "=" + value);
		} else if ((value instanceof Long)) {// 是否是Long类
			mEditor.putLong(key, (Long) value);
			Log.i(TAG, key + "=" + value);
		} else if ((value instanceof Float)) {// 是否是Float类
			mEditor.putFloat(key, (Float) value);
			Log.i(TAG, key + "=" + value);
		}
		mEditor.commit();
	}

	/** 根据Key从SharedPreferences中取数据
	 * 
	 * @param key
	 * @param type
	 * @return */
	public static Object getFromSharedPreferences(String key, Object type) {
		try {
			if (type.equals(String.class.getName())) {
				return mSharedPreferences.getString(key, null);
			} else if (type.equals(Integer.class.getName())) {
				return Integer.valueOf(mSharedPreferences.getInt(key, 0));
			} else if (type.equals(Boolean.class.getName())) {
				return Boolean.valueOf(mSharedPreferences.getBoolean(key, false));
			} else if (type.equals(Long.class.getName())) {
				return Long.valueOf(mSharedPreferences.getLong(key, 0l));
			} else if (type.equals(Float.class.getName())) {
				return Float.valueOf(mSharedPreferences.getFloat(key, 0.0f));
			}
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return null;
	}
}