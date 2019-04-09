package com.jackson.log.utils;

/**
 * created by Jackson at 2018/12/6 15:02
 **/
public class JsonTools {

    private static boolean IS_TAB = false;

    public static String stringToJSON(String strJson) {
        // 计数tab的个数
        int tabNum = 0;
        StringBuffer sb = new StringBuffer();
        int length = strJson.length();

        for (int i = 0; i < length; i++) {
            char c = strJson.charAt(i);
            if (c == '{') {
                tabNum++;
                sb.append(c + "\r");
                sb.append(getSpaceOrTab(tabNum));
            } else if (c == '}') {
                tabNum--;
                sb.append("\r");
                sb.append(getSpaceOrTab(tabNum));
                sb.append(c);
            } else if (c == ',') {
                sb.append(c + "\r");
                sb.append(getSpaceOrTab(tabNum));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // 是空格还是tab
    private static String getSpaceOrTab(int tabNum) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tabNum; i++) {
            if (IS_TAB) {
                sb.append('\r');
            } else {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

}
