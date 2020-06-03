package com.changgou.oauth;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

public class CreateJWT {
    @Test
    public void createJWTTest()
    {
        //读取配置文件
        ClassPathResource classPathResource = new ClassPathResource("changgou.jks");
        //加载证书数据，拿到证书内容   KeyStoreKeyFactory创建该对象需要两个参数：1. 读入证书的ClassPathResource对象
        //                                                                    2. 证书密码的字符数组
        KeyStoreKeyFactory keyStoreKeyFactory=new KeyStoreKeyFactory(classPathResource,"changgou".toCharArray());
        //获取证书中的一对密钥： 公钥和私钥  参数是证书的别名
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("changgou","changgou".toCharArray());
        //获取私钥，RSA算法
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
        //载荷
        Map<String,String> map=new HashMap<>();
        map.put("name","tomcat");
        map.put("address","ty");
        map.put("role","admin");
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(map), new RsaSigner(aPrivate));
        System.out.println(jwt.getEncoded());
    }
    @Test
    public void decodeJwt() throws IOException {
        String publicKey="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmJNzvtPz9W0N7EJQSiOQWw88RURUvjxTy/wwR30xG79iVWky85PATKC5ZhhYqEGPKXS2Z+I7DPTtz2twa+YMoABm69H7XmQ6GOXiDOfTAQmO3DcH0NSLLI6A3t2IgVw+KtX1rU/ZlnTWSSjm3qP+RgOL36OinXD/yvXMY2WqkhsbqlVdTPowS27NJG14PPmtUM1I13isuvSnhKIuJdprJDcCuNgkj6lYtjzqY9tPJz7huM0tT8VYAR3wZWS7GOEMuduPGlw+JzVSBiNYZIOdcho3lSMhCNhj+bsQM3lGP96NBnc9TQCOCaphdvhbYWPZYfubzE2e/Pte7OvvFmKLOwIDAQAB-----END PUBLIC KEY-----";
        String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhZGRyZXNzIjoidHkiLCJyb2xlIjoiYWRtaW4iLCJuYW1lIjoidG9tY2F0In0.cPZFVrdThcPHpeJ7WAGyTSJKNszsB8ehAVI_3J1p3VzBnA5mY95PhFiOP9jJ4TpE9e_nrcosqmXw_nWPI9Xn_FULghEiN-0bvemeOBaGAQyWGe6eXooXQ5vieV2DEgQnA2M-leMZ5LDXrNKpIPu-nBvZCF-j7nxAIosqT3Zz6GoN-dEcFS5HzJWO4yvPi_uHavoo_pKgEfSuLt0ebPviU2WsHAmNHcEF_x7VQ5BilbCM3JEueGEqbRH87RoMi7vg2IlpmzoL3JdrvX94LnB9i7gs7N6_RYQIZ2f7f7cewrXcuuUC9hKZGAxuMEz0wrHMpNDBcUTjMaqR_VJJbxMPsA";
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));

        System.out.println(jwt.getClaims());
    }
}
