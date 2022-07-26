package yieon.kakaopay.workplatform.controller

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.*
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import yieon.kakaopay.workplatform.Stubs.testInquiry
import yieon.kakaopay.workplatform.Stubs.testInquiryDetails
import yieon.kakaopay.workplatform.handler.InquiryHandler
import yieon.kakaopay.workplatform.handler.request.InquiryDetailsReq
import yieon.kakaopay.workplatform.handler.request.InquiryReq
import yieon.kakaopay.workplatform.model.SearchForm
import yieon.kakaopay.workplatform.router.InquiryRouter
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
 * summary : Test - InquiryHandler
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@ExtendWith(SpringExtension::class,  RestDocumentationExtension::class)
@WebFluxTest(InquiryHandler::class)
@AutoConfigureRestDocs
class InquiryControllerTests {

    @MockBean
    lateinit var inquiryService: InquiryService

    @MockBean
    lateinit var jwtTokenProvider: JwtTokenProvider

    lateinit var webTestClient: WebTestClient

    lateinit var inquiryHandler: InquiryHandler

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        inquiryHandler = InquiryHandler(
            inquiryService,
            jwtTokenProvider
        )
        webTestClient = WebTestClient
            .bindToRouterFunction(InquiryRouter().inquiryRoutes(inquiryHandler))
            .configureClient()
            .filter(documentationConfiguration(restDocumentation))
            .build()

    }

    @Test
    fun `test getInquiries api`() {

        whenever(
            inquiryService.getInquiries(
                any()
            )
        ).thenReturn(Mono.just(PageImpl(mutableListOf(testInquiry), PageRequest.of(0, 10, Sort.by("createdAt").descending()), 1)))

        val reqBody = SearchForm(
            size = 10,
            page = 0,
            inquiryId = null
        )

        webTestClient
            .post().uri(URI.create("/inquiry/list"))
            .headers {
                it.set(HttpHeaders.AUTHORIZATION, "Bearer TOKEN")
                it.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            }
            .bodyValue(reqBody)
            .exchange()
            .expectStatus().isOk
            .expectBody().consumeWith(
                document(
                    "get-inquiries",
                    requestFields(
                        fieldWithPath("size")
                            .type(JsonFieldType.NUMBER)
                            .description("조회 사이즈"),
                        fieldWithPath("page")
                            .type(JsonFieldType.NUMBER)
                            .description("조회 페이지"),
                        fieldWithPath("inquiryId")
                            .type(JsonFieldType.NUMBER).optional()
                            .description("문의 ID")
                    )
                )
            )
    }

    @Test
    fun `test getInquiryDetails api`() {

        whenever(
            inquiryService.getInquiryDetails(
                any()
            )
        ).thenReturn(Mono.just(PageImpl(mutableListOf(testInquiryDetails), PageRequest.of(0, 10, Sort.by("createdAt").descending()), 1)))

        val reqBody = SearchForm(
            size = 10,
            page = 0,
            inquiryId = 1
        )

        webTestClient
            .post().uri(URI.create("/inquiry/list/details"))
            .headers {
                it.set(HttpHeaders.AUTHORIZATION, "Bearer TOKEN")
                it.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            }
            .bodyValue(reqBody)
            .exchange()
            .expectStatus().isOk
            .expectBody().consumeWith(
                document(
                    "get-inquiry-details",
                    requestFields(
                        fieldWithPath("size")
                            .type(JsonFieldType.NUMBER)
                            .description("조회 사이즈"),
                        fieldWithPath("page")
                            .type(JsonFieldType.NUMBER)
                            .description("조회 페이지"),
                        fieldWithPath("inquiryId")
                            .type(JsonFieldType.NUMBER)
                            .description("문의 ID")
                    )
                )
            )
    }

    @Test
    fun `test createInquiry api`() {

        whenever(
            inquiryService.createInquiry(
                any(),
                anyString(),
                anyString(),
                anyString()
            )
        ).thenReturn(Mono.just(testInquiry))

        val reqBody = InquiryReq(
            content = "내용",
            title = "제목",
            createdBy = "작성자-고객"
        )

        webTestClient
            .post().uri(URI.create("/inquiry"))
            .headers {
                it.set(HttpHeaders.AUTHORIZATION, "Bearer TOKEN")
                it.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            }
            .bodyValue(reqBody)
            .exchange()
            .expectStatus().isOk
            .expectBody().consumeWith(
                document(
                    "create-inquiry",
                    requestFields(
                        fieldWithPath("content")
                            .type(JsonFieldType.STRING)
                            .description("문의 내용"),
                        fieldWithPath("title")
                            .type(JsonFieldType.STRING)
                            .description("문의 제목"),
                        fieldWithPath("createdBy")
                            .type(JsonFieldType.STRING)
                            .description("작성자")
                    ),
                    inquiryResponseFields()
                )
            )
    }

    @Test
    fun `test createInquiryDetails api`() {

        whenever(
            inquiryService.createInquiryDetails(
                any(),
                anyLong(),
                anyString()
            )
        ).thenReturn(Mono.just(testInquiryDetails))

        val reqBody = InquiryDetailsReq(
            inquiryId = 1,
            content = "추가 문의 or 답변",
        )

        webTestClient
            .post().uri(URI.create("/inquiry/details"))
            .headers {
                it.set(HttpHeaders.AUTHORIZATION, "Bearer TOKEN")
                it.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            }
            .bodyValue(reqBody)
            .exchange()
            .expectStatus().isOk
            .expectBody().consumeWith(
                document(
                    "create-inquiry-details",
                    requestFields(
                        fieldWithPath("inquiryId")
                            .type(JsonFieldType.NUMBER)
                            .description("문의 ID"),
                        fieldWithPath("content")
                            .type(JsonFieldType.STRING)
                            .description("추가 문의 or 답변")
                    ),
                    inquiryDetailsResponseFields()
                )
            )
    }

    @Test
    fun `test updateInquiryAssigner api`() {

        whenever(
            inquiryService.updateInquiryAssigner(
                anyLong(),
                anyLong()
            )
        ).thenReturn(Mono.just(testInquiry))

        webTestClient
            .put().uri("/inquiry/assigner/{inquiryId}", 1)
            .headers {
                it.set(HttpHeaders.AUTHORIZATION, "Bearer TOKEN")
                it.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            }
            .exchange()
            .expectStatus().isOk
            .expectBody().consumeWith(
                document(
                    "update-inquiry-assigned",
                    pathParameters(parameterWithName("inquiryId").description("문의 ID"))
                )
            )
    }

    @Test
    fun `test updateInquiryStatus api`() {

        whenever(
            inquiryService.updateInquiryStatus(
                anyLong(),
                anyLong(),
                anyString()
            )
        ).thenReturn(Mono.just(testInquiry))

        webTestClient
            .put().uri("/inquiry/status/{inquiryId}/{status}", 1, "END")
            .headers {
                it.set(HttpHeaders.AUTHORIZATION, "Bearer TOKEN")
                it.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            }
            .exchange()
            .expectStatus().isOk
            .expectBody().consumeWith(
                document(
                    "update-inquiry-status",
                    pathParameters(
                        parameterWithName("inquiryId").description("문의 ID"),
                        parameterWithName("status").description("문의 상태")
                    )
                )
            )
    }

    private fun inquiryResponseFields() = responseFields(
        fieldWithPath("id")
            .type(JsonFieldType.NUMBER)
            .description("문의 ID"),
        fieldWithPath("userId")
            .type(JsonFieldType.NUMBER)
            .description("고객 ID"),
        fieldWithPath("title")
            .type(JsonFieldType.STRING)
            .description("문의 제목"),
        fieldWithPath("createdBy")
            .type(JsonFieldType.STRING)
            .description("문의자 아이디(직접 입력)"),
        fieldWithPath("assigner")
            .type(JsonFieldType.NUMBER).optional()
            .description("담당자 ID"),
        fieldWithPath("status")
            .type(JsonFieldType.STRING)
            .description("문의 상태"),
        fieldWithPath("createdAt")
            .type(JsonFieldType.STRING).optional()
            .description("생성일자"),
        fieldWithPath("updatedAt")
            .type(JsonFieldType.STRING).optional()
            .description("수정일자"),
    )

    private fun inquiryDetailsResponseFields() = responseFields(
        fieldWithPath("id")
            .type(JsonFieldType.NUMBER)
            .description("문의 상세내용 ID"),
        fieldWithPath("inquiryId")
            .type(JsonFieldType.NUMBER)
            .description("문의 ID"),
        fieldWithPath("content")
            .type(JsonFieldType.STRING)
            .description("내용"),
        fieldWithPath("createdBy")
            .type(JsonFieldType.STRING)
            .description("작성자"),
        fieldWithPath("createdAt")
            .type(JsonFieldType.STRING).optional()
            .description("생성일자"),
        fieldWithPath("updatedAt")
            .type(JsonFieldType.STRING).optional()
            .description("수정일자"),
    )

}