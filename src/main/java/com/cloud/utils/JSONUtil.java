package com.cloud.utils;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;

public class JSONUtil {


    public static String toJsonString(Object obj) {
        // 创建一个 ByteArrayOutputStream，用于存储 XML 数据
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // 创建 XMLEncoder 对象，将对象编码为 XML
        XMLEncoder encoder = new XMLEncoder(out);

        // 将对象写入编码器
        encoder.writeObject(obj);

        // 关闭编码器
        encoder.close();

        // 返回 JSON 字符串
        return out.toString();
    }
}
