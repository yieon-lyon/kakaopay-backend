package yieon.kakaopay.workplatform.service

import org.springframework.data.domain.Page
import reactor.core.publisher.Mono
import yieon.kakaopay.workplatform.entity.Inquiry
import yieon.kakaopay.workplatform.entity.InquiryDetails
import yieon.kakaopay.workplatform.model.SearchForm
import yieon.kakaopay.workplatform.model.TokenToUserInfo

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : InquiryService
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
interface InquiryService {

    fun getInquiry(inquiryId: Long): Mono<Inquiry>
    fun getInquiries(condition: SearchForm): Mono<Page<Inquiry>>
    fun getInquiryDetails(condition: SearchForm): Mono<Page<InquiryDetails>>
    fun createInquiry(userInfo: TokenToUserInfo, title: String, content: String, createdBy: String): Mono<Inquiry>
    fun createInquiryDetails(userInfo: TokenToUserInfo, inquiryId: Long, content: String): Mono<InquiryDetails>

    fun checkInquiryAssigner(inquiryId: Long, admin_id: Long): Boolean

    fun updateInquiryAssigner(inquiryId: Long, admin_id: Long): Mono<Inquiry>
    fun updateInquiryStatus(inquiryId: Long, admin_id: Long, status: String): Mono<Inquiry>

}