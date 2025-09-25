package com.weiwei.lease.common.utils;

import com.weiwei.lease.common.exception.BusinessException;
import com.weiwei.lease.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static long tokenExpiration = 60 * 60 * 1000L;
    private static SecretKey tokenSignKey = Keys.hmacShaKeyFor("M0PKKI6pYGVWWfDZw90a0lTpGYX1d4AQ".getBytes());

    public static String createToken(Long userId, String username) {
        String token = Jwts.builder().
                setSubject("USER_INFO").
                setExpiration(new Date(System.currentTimeMillis() + tokenExpiration)).
                claim("userId", userId).//自定义信息
                claim("username", username).
                signWith(tokenSignKey).
                compact();
        return token;
    }

    public static Claims parseToken(String token){

        if (token==null){
            throw new BusinessException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        try{
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(tokenSignKey).build();
            return jwtParser.parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            throw new BusinessException(ResultCodeEnum.TOKEN_EXPIRED);
        }catch (JwtException e){
            throw new BusinessException(ResultCodeEnum.TOKEN_INVALID);
        }
    }

    public static void main(String[] args) {
        String token = createToken(1L, "13888888888");
        System.out.println(token);
    }
}
