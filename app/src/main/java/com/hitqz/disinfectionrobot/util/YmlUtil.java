package com.hitqz.disinfectionrobot.util;

import android.util.Log;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

public class YmlUtil {
    public static final String TAG = YmlUtil.class.getSimpleName();

    public static Map<String, Object> parseYaml(String yamlPath) {
        Map<String, Object> map = null;
        try {
            Yaml yaml = new Yaml();
            File file = new File(yamlPath);
            if (file.exists()) {
                //将值转换为Map
                map = (Map<String, Object>) yaml.load(new FileInputStream(file));
            }
        } catch (Exception e) {
            Log.e(TAG, "地图转换读取yaml失败：" + e.getMessage());
            return map;
        }
        return map;
    }
}
