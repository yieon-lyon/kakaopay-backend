package yieon.kakaopay.workplatform.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import yieon.kakaopay.workplatform.entity.*
import yieon.kakaopay.workplatform.entity.enum.Role
import yieon.kakaopay.workplatform.repository.*
import java.time.LocalDateTime

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : UserServiceImpl
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    fun generateDefaultRole() = mutableSetOf(
        Authority().apply {
            name = Role.ROLE_USER
        }
    )

    override fun signUp(email: String, password: String, name: String, login: String): Mono<User> {
        val user = User()
            .apply {
                this.email = email
                this.password = passwordEncoder.encode(password)
                this.name = name
                this.login = login
                this.authorities = generateDefaultRole()
            }

        return Mono.defer { Mono.just(userRepository.save(user)) }
            .subscribeOn(Schedulers.boundedElastic())
    }

    override fun checkIfAlreadyExistUser(email: String): Mono<Boolean> {
        return Mono.defer { Mono.justOrEmpty(userRepository.findAllByEmail(email)) }
            .flatMap {
                Mono.just(it.isNotEmpty())
            }
    }

    override fun signIn(login: String, password: String): Mono<User> {
        return Mono.defer { Mono.justOrEmpty(userRepository.findByLogin(login)) }
            .flatMap {
                if (passwordEncoder.matches(password, it.password)) {
                    Mono.just(it)
                } else {
                    Mono.empty()
                }
            }
            .map { user ->
                user.apply { lastSignAt = LocalDateTime.now() }
            }
            .flatMap {
                Mono.defer { Mono.just(userRepository.save(it)) }
            }
            .subscribeOn(Schedulers.boundedElastic())
    }

}