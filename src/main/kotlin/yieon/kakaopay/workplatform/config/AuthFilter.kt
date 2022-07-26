package yieon.kakaopay.workplatform.config

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import org.springframework.web.util.pattern.PathPattern
import org.springframework.web.util.pattern.PathPatternParser
import reactor.core.publisher.Mono
import yieon.kakaopay.workplatform.util.JwtTokenProvider

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : AuthFilter
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Component
class AuthFilter(private val jwtTokenProvider: JwtTokenProvider): WebFilter {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    private var pathPattern: PathPattern = PathPatternParser().parse("/inquiry/**")

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {

        logger.info("Request ${exchange.request.path.value()} called")

        return if (!pathPattern.matches(exchange.request.path.pathWithinApplication())) {
            chain.filter(exchange)
        } else {
            checkAuthorization(exchange.request)
                .doOnError { exchange.response.statusCode = HttpStatus.UNAUTHORIZED }
                .then(chain.filter(exchange))
        }
    }

    private fun checkAuthorization(request: ServerHttpRequest): Mono<String> {

        request.headers.getFirst("Authorization")?.let {
            return if (jwtTokenProvider.tokenValidate(it.substring("Bearer ".length))) {
                Mono.just(it)
            } else {
                Mono.error(UnauthorizedException("Unauthorized"))
            }
        }
        return Mono.error(UnauthorizedException("Unauthorized"))
    }
}

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
class UnauthorizedException(message: String?) : RuntimeException(message)