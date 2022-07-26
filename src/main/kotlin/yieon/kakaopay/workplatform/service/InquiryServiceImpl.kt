package yieon.kakaopay.workplatform.service

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import yieon.kakaopay.workplatform.entity.*
import yieon.kakaopay.workplatform.entity.enum.InquiryStatus
import yieon.kakaopay.workplatform.entity.enum.Role
import yieon.kakaopay.workplatform.handler.response.ResponseCode
import yieon.kakaopay.workplatform.model.SearchForm
import yieon.kakaopay.workplatform.model.TokenToUserInfo
import yieon.kakaopay.workplatform.repository.InquiryDetailsRepository
import yieon.kakaopay.workplatform.repository.InquiryDetailsRepositorySupport
import yieon.kakaopay.workplatform.repository.InquiryRepository
import yieon.kakaopay.workplatform.repository.InquiryRepositorySupport

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : InquiryServiceImpl
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Service
class InquiryServiceImpl(
    private val inquiryRepository: InquiryRepository,
    private val inquiryRepositorySupport: InquiryRepositorySupport,
    private val inquiryDetailsRepository: InquiryDetailsRepository,
    private val inquiryDetailsRepositorySupport: InquiryDetailsRepositorySupport
) : InquiryService {

    override fun getInquiry(inquiryId: Long): Mono<Inquiry> {
        return Mono.defer {
            Mono.just(inquiryRepository.findById(inquiryId).orElse(null))
        }
    }

    override fun getInquiries(condition: SearchForm): Mono<Page<Inquiry>> {
        return Mono.defer {
            Mono.just(inquiryRepositorySupport.findAll(condition))
        }
    }

    override fun getInquiryDetails(condition: SearchForm): Mono<Page<InquiryDetails>> {
        return Mono.defer {
            Mono.just(inquiryDetailsRepositorySupport.findDetailsByInquiryId(condition))
        }
    }

    override fun createInquiry(userInfo: TokenToUserInfo, title: String, content: String, createdBy: String): Mono<Inquiry> {
        val inquiry = inquiryRepository.save(Inquiry().apply {
            this.userId = userInfo.id
            this.title = title
            this.createdBy = createdBy
        })
        inquiryDetailsRepository.save(InquiryDetails().apply {
            this.inquiryId = inquiry.id
            this.content = content
            this.createdBy = userInfo.name
        })
        return inquiry.toMono()
    }

    override fun createInquiryDetails(userInfo: TokenToUserInfo, inquiryId: Long, content: String): Mono<InquiryDetails> {
        val inquiryDetails = inquiryDetailsRepository.save(InquiryDetails().apply {
            this.inquiryId = inquiryId
            this.content = content
            this.createdBy = userInfo.name
        })

        if (userInfo.roles.contains(Role.ROLE_ADMIN.roleName)) {
            val inquiry = inquiryRepository.findById(inquiryId).orElse(null)
            inquiry.status = InquiryStatus.ING
            inquiryRepository.save(inquiry)
        }

        return inquiryDetails.toMono()
    }

    override fun checkInquiryAssigner(inquiryId: Long, admin_id: Long): Boolean {
        val inquiry = inquiryRepository.findById(inquiryId).orElse(null)
        return inquiry?.assigner != null && inquiry?.assigner == admin_id
    }

    override fun updateInquiryAssigner(inquiryId: Long, admin_id: Long): Mono<Inquiry> {
        val inquiry = inquiryRepository.findById(inquiryId).orElse(null)
        return if (inquiry != null) {
            if (inquiry.assigner != null && inquiry.assigner != admin_id) {
                Mono.error(Exception(ResponseCode.ALREADY_ASSIGNER.msg))
            } else {
                inquiry.assigner = admin_id
                inquiryRepository.save(inquiry)
                inquiry.toMono()
            }
        } else {
            Mono.error(Exception(ResponseCode.NOT_SELECTED_ASSIGNER.msg))
        }
    }

    override fun updateInquiryStatus(inquiryId: Long, admin_id: Long, status: String): Mono<Inquiry> {
        if (!checkInquiryAssigner(inquiryId, admin_id)) {
            return Mono.error(Exception(ResponseCode.NOT_SELECTED_ASSIGNER.msg))
        }
        val inquiry = inquiryRepository.findById(inquiryId).orElse(null)
        inquiry.status = InquiryStatus.valueOf(status)
        inquiryRepository.save(inquiry)
        return inquiry.toMono()
    }

}