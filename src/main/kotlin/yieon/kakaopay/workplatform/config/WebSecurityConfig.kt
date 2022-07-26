package yieon.kakaopay.workplatform.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : WebSecurityConfig
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
class WebSecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(11)
    }

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable()
            .build()
    }
}