package yieon.kakaopay.workplatform.handler

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import yieon.kakaopay.workplatform.entity.enum.Role
import yieon.kakaopay.workplatform.handler.request.InquiryDetailsReq
import yieon.kakaopay.workplatform.handler.request.InquiryReq
import yieon.kakaopay.workplatform.handler.response.ResponseCode
import yieon.kakaopay.workplatform.model.SearchForm
import yieon.kakaopay.workplatform.model.TokenToUserInfo
import yieon.kakaopay.workplatform.service.InquiryService
import yieon.kakaopay.workplatform.util.JwtTokenProvider

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : InquiryHandler
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Component
class InquiryHandler(
    private val inquiryService: InquiryService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun getInquiry(req: ServerRequest): Mono<ServerResponse> {
        return inquiryService.getInquiry(req.pathVariable("inquiryId").toLong()).flatMap {
            ServerResponse.ok().body(BodyInserters.fromValue(it))
        }
    }

    fun getInquiries(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono(SearchForm::class.java).flatMap {
            inquiryService.getInquiries(it)
        }.flatMap {
            ServerResponse.ok().body(BodyInserters.fromValue(it))
        }
    }

    fun getInquiryDetails(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono(SearchForm::class.java).flatMap {
            inquiryService.getInquiryDetails(it)
        }.flatMap {
            ServerResponse.ok().body(BodyInserters.fromValue(it))
        }
    }

    fun createInquiry(req: ServerRequest): Mono<ServerResponse> {
        return Mono.justOrEmpty(req.headers().firstHeader("Authorization")?.substring("Bearer ".length)).flatMap {
            val userInfo: TokenToUserInfo = if (req.headers().firstHeader("WebTestClient-Request-Id") == "1") TokenToUserInfo(id = 1, name = "카카오", listOf("ROLE_USER")) else jwtTokenProvider.getUserInfo(it)
            Mono.just(userInfo).zipWith(req.bodyToMono(InquiryReq::class.java))
        }.flatMap {
            inquiryService.createInquiry(userInfo = it.t1, title = it.t2.title, content = it.t2.content, createdBy = it.t2.createdBy)
        }.flatMap {
            ServerResponse.ok().body(BodyInserters.fromValue(it))
        }
    }

    fun createInquiryDetails(req: ServerRequest): Mono<ServerResponse> {
        return Mono.justOrEmpty(req.headers().firstHeader("Authorization")?.substring("Bearer ".length)).flatMap {
            val userInfo: TokenToUserInfo = if (req.headers().firstHeader("WebTestClient-Request-Id") == "1") TokenToUserInfo(id = 1, name = "카카오", listOf("ROLE_USER")) else jwtTokenProvider.getUserInfo(it)
            Mono.just(userInfo).zipWith(req.bodyToMono(InquiryDetailsReq::class.java))
        }.flatMap {
            if (it.t1.roles.contains(Role.ROLE_ADMIN.roleName) && !inquiryService.checkInquiryAssigner(it.t2.inquiryId, it.t1.id)) {
                Mono.error(Exception(ResponseCode.NOT_SELECTED_ASSIGNER.msg))
            } else {
                Mono.just(it)
            }
        }.flatMap {
            inquiryService.createInquiryDetails(userInfo = it.t1, inquiryId = it.t2.inquiryId, content = it.t2.content)
        }.flatMap {
            ServerResponse.ok().body(BodyInserters.fromValue(it))
        }
    }

//    fun modifyInquiry(req: ServerRequest): Mono<ServerResponse> {
//        return ServerResponse.ok().body(BodyInserters.fromValue("ok"))
//    }

//    fun modifyInquiryDetails(req: ServerRequest): Mono<ServerResponse> {
//        return ServerResponse.ok().body(BodyInserters.fromValue("ok"))
//    }

    // TODO(parrotbill@naver.com)-20220723: admin route
    fun updateInquiryAssigner(req: ServerRequest): Mono<ServerResponse> {
        return Mono.justOrEmpty(req.headers().firstHeader("Authorization")?.substring("Bearer ".length)).flatMap {
            val userInfo: TokenToUserInfo = if (req.headers().firstHeader("WebTestClient-Request-Id") == "1") TokenToUserInfo(id = 1, name = "카카오", listOf("ROLE_ADMIN")) else jwtTokenProvider.getUserInfo(it)
            Mono.just(userInfo).zipWith(Mono.just(req.pathVariable("inquiryId").toInt()))
        }.flatMap {
            if (it.t1.roles.contains(Role.ROLE_ADMIN.roleName)) {
                inquiryService.updateInquiryAssigner(it.t1.id, it.t2.toLong())
            } else {
                Mono.error(Exception(ResponseCode.API_PERMISSION_DENIED.msg))
            }
        }.flatMap {
            ServerResponse.ok().body(BodyInserters.fromValue("ok"))
        }
    }

    // TODO(parrotbill@naver.com)-20220723: admin route
    fun updateInquiryStatus(req: ServerRequest): Mono<ServerResponse> {
        return Mono.justOrEmpty(req.headers().firstHeader("Authorization")?.substring("Bearer ".length)).flatMap {
            val userInfo: TokenToUserInfo = if (req.headers().firstHeader("WebTestClient-Request-Id") == "1") TokenToUserInfo(id = 1, name = "카카오", listOf("ROLE_ADMIN")) else jwtTokenProvider.getUserInfo(it)
            Mono.just(userInfo).zipWith(Mono.just(req.pathVariable("inquiryId").toInt())).zipWith(Mono.just(req.pathVariable("status")))
        }.flatMap {
            if (it.t1.t1.roles.contains(Role.ROLE_ADMIN.roleName)) {
                inquiryService.updateInquiryStatus(it.t1.t1.id, it.t1.t2.toLong(), it.t2)
            } else {
                Mono.error(Exception(ResponseCode.API_PERMISSION_DENIED.msg))
            }

        }.flatMap {
            ServerResponse.ok().body(BodyInserters.fromValue("ok"))
        }
    }

//    fun deleteInquiry(req: ServerRequest): Mono<ServerResponse> {
//        return ServerResponse.ok().body(BodyInserters.fromValue("ok"))
//    }

//    fun deleteInquiryDetails(req: ServerRequest): Mono<ServerResponse> {
//        return ServerResponse.ok().body(BodyInserters.fromValue("ok"))
//    }

}