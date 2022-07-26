package yieon.kakaopay.workplatform.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import yieon.kakaopay.workplatform.util.JwtTokenProvider

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : JwtConfig
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Configuration
class JwtConfig {
    @Bean
    fun jwtParseUtil(@Value("\${jwt.key}") key: String) = JwtTokenProvider(key)
}
