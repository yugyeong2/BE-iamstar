package com.yugyeong.iamstar.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {
    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    fun generateToken(username: String): String {
        val token = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10시간 유효
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
        println("Generated Token: $token") // 토큰 생성 로깅
        return token
    }

    fun validateToken(token: String, username: String): Boolean {
        val claims = extractAllClaims(token)
        val tokenUsername = claims.subject
        val isValid = (username == tokenUsername && !isTokenExpired(token))
        println("Token Validation: $isValid") // 토큰 유효성 검사 로깅
        return isValid
    }

    internal fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = extractAllClaims(token).expiration
        return expiration.before(Date())
    }
}
