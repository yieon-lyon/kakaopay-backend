package yieon.kakaopay.workplatform.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router
import yieon.kakaopay.workplatform.handler.InquiryHandler

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : InquiryRouter
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Configuration
class InquiryRouter {

    @Bean
    fun inquiryRoutes(ih: InquiryHandler) = nest(path("/inquiry"),
        router {
            listOf(
                GET("/{inquiryId}", ih::getInquiry),
                POST("/list", ih::getInquiries),
                POST("/list/details", ih::getInquiryDetails)
            )
            listOf(
                POST("/", ih::createInquiry),
                POST("/details", ih::createInquiryDetails)
            )
            listOf(
//                PUT("/", ih::modifyInquiry),
//                PUT("/details", ih::modifyInquiryDetails),
                PUT("/assigner/{inquiryId}", ih::updateInquiryAssigner),
                PUT("/status/{inquiryId}/{status}", ih::updateInquiryStatus)
            )
//            listOf(
//                DELETE("/", ih::deleteInquiry),
//                DELETE("/details", ih::deleteInquiryDetails)
//            )
        }
    )
}