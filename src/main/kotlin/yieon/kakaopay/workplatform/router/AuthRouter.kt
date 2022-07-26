package yieon.kakaopay.workplatform.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router
import yieon.kakaopay.workplatform.handler.AuthHandler

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : AuthRouter
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Configuration
class AuthRouter {

    @Bean
    fun authRoutes(ah: AuthHandler) = nest(path("/auth"),
        router {
            listOf(
                POST("/signup", ah::signUp),
                POST("/signin", ah::signIn)
            )
        }
    )

    @Bean
    fun htmlRouter() = nest(path("/docs"),
        router {
            resources("/**", ClassPathResource("static/docs/"))
        }
    )

}