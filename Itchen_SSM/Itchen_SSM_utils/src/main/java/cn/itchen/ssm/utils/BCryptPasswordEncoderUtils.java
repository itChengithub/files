package cn.itchen.ssm.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密的工具类
 */
public class BCryptPasswordEncoderUtils {
    private static BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    public static String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

    public static void main(String[] args) {
        String s="zsy123321";
        String s1 = encodePassword(s);
        System.out.println(s1);
    }
}
