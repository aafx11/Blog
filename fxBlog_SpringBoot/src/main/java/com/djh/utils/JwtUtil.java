package com.djh.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@ConfigurationProperties(prefix = "djh.jwt")
public class JwtUtil {

    private long expire;
    private String secretKey;
    private String header;


    /*生成jwt:
    *   1.设置类型，表面是jwt
    *   2.设置主体部分
    *   3.设置创建时间和过期时间
    *   4.设置加密算法和密钥
    * */
    public String generateJwtToken(String username){

        Date nowDate = new Date();
        Date expirationDate = new Date(nowDate.getTime()+ 1000 * expire);

        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512,secretKey)
                .compact();
    }


    /*解析jwt，用于后端认证jwt：
    *   1.设置密钥
    *   2.解析jwt
    *   3.获取Claims主体
    *   4.如果jwt不合法，则抛出异常
    * */
    public Claims getClaimByToken(String jwt){
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt)
                    .getBody();
        }catch (Exception e){
            return null;
        }
    }

    /*判断jwt是否过期：
    *   1.如果当前时间是jwt过期时间之前，则返回true，否则返回false
    * */
    public boolean jwtExpired(Claims claims){
        return claims.getExpiration().before(new Date());
    }
}
