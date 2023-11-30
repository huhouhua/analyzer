package org.ruijie.core.docker;

import cn.hutool.core.util.StrUtil;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;

class Helper {

    public static void replacTextContent(File file, String patternString, String replaceStr) throws IOException {

        FileReader in = new FileReader(file);
        BufferedReader bufIn = new BufferedReader(in);
        // 内存流, 作为临时流
        CharArrayWriter tempStream = new CharArrayWriter();
        // 替换
        String line = null;
        while ( (line = bufIn.readLine()) != null) {
            // 替换每行中, 符合条件的字符串
            line = line.replaceAll(patternString, Matcher.quoteReplacement(replaceStr));
            // 将该行写入内存
            tempStream.write(line);
            // 添加换行符
            tempStream.append(System.getProperty("line.separator"));
        }
        // 关闭 输入流
        bufIn.close();
        // 将内存中的流 写入 文件
        FileWriter out = new FileWriter(file);
        tempStream.writeTo(out);
        out.close();
    }

    public  static  List<String> ofVariableFormMap(Map<String,String> envMap){
        List<String> list = new ArrayList<>(envMap.size());
        Iterator<Map.Entry<String, String>> iterator = envMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            list.add(StrUtil.format("{}={}",entry.getKey(),entry.getValue()));
        }
        return list;
    }
}
