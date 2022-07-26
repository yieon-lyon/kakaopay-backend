package yieon.kakaopay.workplatform.handler

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.onErrorResume
import reactor.kotlin.core.publisher.switchIfEmpty
import yieon.kakaopay.workplatform.exception.*
import yieon.kakaopay.workplatform.handler.request.*
import yieon.kakaopay.workplatform.handler.response.ResponseCode
import yieon.kakaopay.workplatform.handler.response.TokenResp
import yieon.kakaopay.workplatform.service.UserService
import yieon.kakaopay.workplatform.util.JwtTokenProvider

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : AuthHandler
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Component
class AuthHandler(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun signUp(req: ServerRequest): Mono<ServerResponse> {

        return req.bodyToMono(SignUpReq::class.java)
            .flatMap {
                if (it.password != it.passwordConfirmation) {
                    Mono.error<PasswordNotMatchingException>(PasswordNotMatchingException())
                } else {
                    Mono.just(it)
                }
            }
            .flatMap {
                if ((it as SignUpReq).password.length < 6) {
                    Mono.error<TooShortPasswordException>(TooShortPasswordException())
                } else {
                    Mono.just(it)
                }
            }
            .flatMap {
                (it as SignUpReq)
                Mono.just(it).zipWith(userService.checkIfAlreadyExistUser(it.email))
            }
            .flatMap { tuple ->
                val signUpReq = tuple.t1
                val isExist = tuple.t2
                if (isExist) {
                    Mono.error<AlreadyExistEmailException>(AlreadyExistEmailException())
                } else {
                    userService.signUp(
                        email = signUpReq.email,
                        password = signUpReq.password,
                        name = signUpReq.name,
                        login = signUpReq.login
                    )
                }
            }
            .flatMap {
                ServerResponse.ok().build()
            }
            .onErrorResume(AlreadyExistEmailException::class) {
                ServerResponse.badRequest().body(
                    BodyInserters.fromValue(
                        ResponseCode.ALREADY_EXIST_EMAIL
                    )
                )
            }
            .onErrorResume(PasswordNotMatchingException::class) {
                ServerResponse.badRequest().body(
                    BodyInserters.fromValue(ResponseCode.INVALID_CREDENTIALS)
                )
            }
            .onErrorResume(TooShortPasswordException::class) {
                ServerResponse.badRequest().body(
                    BodyInserters.fromValue(
                        ResponseCode.TOO_SHORT_PASSWORD
                    )
                )
            }
    }

    fun signIn(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono(LoginReq::class.java)
            .flatMap { userService.signIn(it.login.toLowerCase(), it.password) }
            .flatMap { ServerResponse.ok().body(BodyInserters.fromValue(TokenResp(jwtTokenProvider.createToken(it)))) }
            .switchIfEmpty {
                ServerResponse.badRequest().body(
                    BodyInserters.fromValue(ResponseCode.INVALID_CREDENTIALS)
                )
            }
    }

}