package yieon.kakaopay.workplatform.service

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import yieon.kakaopay.workplatform.entity.User
import yieon.kakaopay.workplatform.repository.*
import yieon.kakaopay.workplatform.util.JwtTokenProvider
import kotlin.test.assertNotNull

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Test - AuthService
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@ExtendWith(MockitoExtension::class)
class AuthServiceTests {

    var userRepository: UserRepository = mock()

    var passwordEncoder: PasswordEncoder = mock()

    private var jwtTokenProvider: JwtTokenProvider = mock()

    lateinit var userService: UserService

    private lateinit var testUser: User

    @BeforeEach
    fun setUp() {
        userService = UserServiceImpl(
            userRepository,
            passwordEncoder
        )
        testUser = User().apply {
            email = "test@test.com"
            password = "password"
            name = "카카오"
            login = "test"
        }
    }

    @Test
    fun `test signup`() {
        whenever(passwordEncoder.encode(anyString())).thenReturn(anyString())

        userService.signUp(
            email = "test@test.com",
            password = "password",
            name = "name",
            login = "test"
        ).subscribe {
            verify(userRepository).save(testUser)
        }
    }

    @Test
    fun `test signin`() {

        testUser.password = "{bcrypt}password"
        whenever(passwordEncoder.matches(anyString(), anyString()))
            .thenReturn(true)
        whenever(jwtTokenProvider.createToken(testUser)).thenReturn("token")

        userService.signIn("test@test.com", "password")
            .subscribe {
                assertNotNull(it)
            }
    }

}