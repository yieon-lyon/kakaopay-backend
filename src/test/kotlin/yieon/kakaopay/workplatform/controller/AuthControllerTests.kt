package yieon.kakaopay.workplatform.controller

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import yieon.kakaopay.workplatform.Stubs.testUser
import yieon.kakaopay.workplatform.entity.User
import yieon.kakaopay.workplatform.handler.AuthHandler
import yieon.kakaopay.workplatform.handler.request.LoginReq
import yieon.kakaopay.workplatform.handler.request.SignUpReq
import yieon.kakaopay.workplatform.router.AuthRouter
import yieon.kakaopay.workplatform.service.*
import yieon.kakaopay.workplatform.util.JwtTokenProvider
import java.net.URI

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Test - AuthHandler
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@ExtendWith(SpringExtension::class,  RestDocumentationExtension::class)
@WebFluxTest(AuthHandler::class)
@AutoConfigureRestDocs
class AuthControllerTests {

    @MockBean
    lateinit var userService: UserService

    @MockBean
    lateinit var jwtTokenProvider: JwtTokenProvider

    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var authHandler: AuthHandler

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {

        webTestClient = WebTestClient
            .bindToRouterFunction(AuthRouter().authRoutes(authHandler))
            .configureClient()
            .filter(documentationConfiguration(restDocumentation))
            .build()

    }



    @Test
    fun `test signup api with existing email`() {
        whenever(userService.checkIfAlreadyExistUser(anyString())).thenReturn(Mono.just(true))

        val reqBody = SignUpReq(
            email = "test@test.com",
            password = "password",
            passwordConfirmation = "password",
            name = "name",
            login = "test"
        )

        webTestClient
            .post().uri(URI.create("/auth/signup"))
            .bodyValue(reqBody)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody().consumeWith(
                document(
                    "signup-with-existing-email",
                    signUpRequestFields()
                )
            )
    }

    @Test
    fun `test signup api`() {

        whenever(userService.checkIfAlreadyExistUser(anyString())).thenReturn(Mono.just(false))
        whenever(
            userService.signUp(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
        ).thenReturn(Mono.just(testUser))

        whenever(jwtTokenProvider.createToken(eq(testUser))).thenReturn("token")

        val reqBody = SignUpReq(
            email = "test@test.com",
            password = "password",
            passwordConfirmation = "password",
            name = "카카오",
            login = "test"
        )

        webTestClient
            .post().uri(URI.create("/auth/signup"))
            .bodyValue(reqBody)
            .exchange()
            .expectStatus().isOk
            .expectBody().consumeWith(
                document(
                    "signup",
                    signUpRequestFields()
                )
            )
    }

    @Test
    fun `test signin api with valid user`() {

        val mockUser = User().apply {
            email = "test@test.com"
            password = "password"
            name = "카카오"
            login = "test"
        }

        whenever(userService.signIn(anyString(), anyString()))
            .thenReturn(Mono.just(mockUser))
        whenever(jwtTokenProvider.createToken(any()))
            .thenReturn("refreshToken")
        val reqBody = LoginReq("test", "password")

        webTestClient
            .post().uri(URI.create("/auth/signin"))
            .bodyValue(reqBody)
            .exchange()
            .expectStatus().isOk
            .expectBody().consumeWith(
                document(
                    "signin",
                    requestFields(
                        fieldWithPath("login")
                            .type(JsonFieldType.STRING)
                            .description("로그인"),
                        fieldWithPath("password")
                            .type(JsonFieldType.STRING)
                            .description("비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("token")
                            .type(JsonFieldType.STRING)
                            .description("access token")
                    )
                )
            )
    }

    @Test
    fun `test signin api with invalid user`() {

        whenever(userService.signIn(anyString(), anyString()))
            .thenReturn(Mono.empty())

        val reqBody = LoginReq("test", "password")

        webTestClient
            .post().uri(URI.create("/auth/signin"))
            .bodyValue(reqBody)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody().consumeWith(
                document(
                    "signin-with-invalid-user",
                    requestFields(
                        fieldWithPath("login")
                            .type(JsonFieldType.STRING)
                            .description("로그인"),
                        fieldWithPath("password")
                            .type(JsonFieldType.STRING)
                            .description("비밀번호")
                    )
                )
            )
    }

    private fun signUpRequestFields() = requestFields(
        fieldWithPath("email")
            .type(JsonFieldType.STRING)
            .description("이메일"),
        fieldWithPath("password")
            .type(JsonFieldType.STRING)
            .description("비밀번호"),
        fieldWithPath("passwordConfirmation")
            .type(JsonFieldType.STRING)
            .description("비밀번호 확인"),
        fieldWithPath("name")
            .type(JsonFieldType.STRING)
            .description("사용자 이름"),
        fieldWithPath("login")
            .type(JsonFieldType.STRING)
            .description("로그인")
    )

}