package com.yugyeong.iamstar.controller

import com.yugyeong.iamstar.dto.SignInRequest
import com.yugyeong.iamstar.dto.SignUpRequest
import com.yugyeong.iamstar.model.User
import com.yugyeong.iamstar.service.UserService
import com.yugyeong.iamstar.util.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder

) {

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@RequestBody signupRequest: SignUpRequest) {
        val user = User(
            email = signupRequest.email,
            password = passwordEncoder.encode(signupRequest.password),
            username = signupRequest.username,
            fullName = signupRequest.fullName
        )
        userService.save(user)
    }

    @PostMapping("/signin")
    fun signin(@RequestBody signInRequest: SignInRequest): String {
        val authenticationToken = UsernamePasswordAuthenticationToken(signInRequest.email, signInRequest.password)
        authenticationManager.authenticate(authenticationToken)

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(signInRequest.email)

        // 비밀번호 비교
        if (passwordEncoder.matches(signInRequest.password, userDetails.password)) {
            return jwtUtil.generateToken(userDetails.username)
        } else {
            throw RuntimeException("Invalid credentials")
        }
    }
}
