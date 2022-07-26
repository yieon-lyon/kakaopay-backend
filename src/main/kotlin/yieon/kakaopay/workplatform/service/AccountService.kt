package yieon.kakaopay.workplatform.service

import reactor.core.publisher.Mono
import yieon.kakaopay.workplatform.entity.User

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : UserService
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
interface UserService {
    fun signUp(email: String, password: String, name: String, login: String): Mono<User>
    fun checkIfAlreadyExistUser(email: String): Mono<Boolean>
    fun signIn(login: String, password: String): Mono<User>
}