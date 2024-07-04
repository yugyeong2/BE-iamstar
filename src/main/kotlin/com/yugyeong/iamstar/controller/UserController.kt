package com.yugyeong.iamstar.controller

import com.yugyeong.iamstar.dto.SignInRequest
import com.yugyeong.iamstar.dto.SignUpRequest
import com.yugyeong.iamstar.model.User
import com.yugyeong.iamstar.service.UserService
import com.yugyeong.iamstar.util.JwtUtil
import com.yugyeong.iamstar.service.UserDetailsService
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.slf4j.LoggerFactory

@RestController
class UserController(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder
) {
    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@RequestBody signupRequest: SignUpRequest): Map<String, String> {
        val encodedPassword = passwordEncoder.encode(signupRequest.password)
        val user = User(
            email = signupRequest.email,
            password = encodedPassword,
            username = signupRequest.username,
            fullName = signupRequest.fullName
        )
        userService.save(user)
        logger.info("사용자 가입 성공: ${signupRequest.email}")
        logger.info("사용자가 회원가입한 비밀번호: ${signupRequest.password}")
        logger.info("저장된 해시된 비밀번호: $encodedPassword")

    }

    @PostMapping("/signin")
    fun signin(@RequestBody signInRequest: SignInRequest): String {
        try {
            val userDetails: UserDetails = userDetailsService.loadUserByUsername(signInRequest.email)
            logger.info("로그인 시 입력한 이메일: ${signInRequest.email}")

            // 비밀번호 비교
            val matches = passwordEncoder.matches(signInRequest.password, userDetails.password)
            logger.info("로그인 시 입력한 비밀번호: ${signInRequest.password}")
            logger.info("데이터베이스에 저장된 해시된 비밀번호: ${userDetails.password}")
            logger.info("비밀번호 일치 여부: $matches")

            if (matches) {
                logger.info("비밀번호 매칭 성공")
                val authenticationToken = UsernamePasswordAuthenticationToken(signInRequest.email, signInRequest.password)
                authenticationManager.authenticate(authenticationToken)
                logger.info("사용자 인증 성공: ${signInRequest.email}")

                val token = jwtUtil.generateToken(userDetails.username)
                logger.info("JWT 토큰 생성 성공: ${signInRequest.email}")
                return token
            } else {
                logger.error("비밀번호 불일치: ${signInRequest.email}")
                throw RuntimeException("비밀번호가 일치하지 않습니다.")
            }
        } catch (e: BadCredentialsException) {
            logger.error("로그인 실패: 자격 증명에 실패하였습니다.")
            throw RuntimeException("자격 증명에 실패하였습니다. 이메일과 비밀번호를 확인해주세요.")
        } catch (e: Exception) {
            logger.error("로그인 중 예기치 않은 오류 발생: ${e.message}")
            throw RuntimeException("로그인 중 오류가 발생했습니다. 다시 시도해주세요.")
        }
    }
}
