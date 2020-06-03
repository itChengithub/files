package com.changgou.filter;

public class UrlFilter {
    private static final String URL="/user/login,/api/user/add";
    public static boolean testUrl(String url){
        String[] urls = URL.split(",");
        for (String u : urls) {
            if(u.equals(url)){
                return false;
            }
        }
        return true;
    }
}
