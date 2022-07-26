package yieon.kakaopay.workplatform.service

import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import yieon.kakaopay.workplatform.entity.Inquiry
import yieon.kakaopay.workplatform.entity.InquiryDetails
import yieon.kakaopay.workplatform.entity.enum.InquiryStatus
import yieon.kakaopay.workplatform.model.SearchForm
import yieon.kakaopay.workplatform.model.TokenToUserInfo
import yieon.kakaopay.workplatform.repository.*

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Test - InquiryService
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@ExtendWith(MockitoExtension::class)
class InquiryServiceTests {

    var inquiryRepository: InquiryRepository = mock()

    var inquiryRepositorySupport: InquiryRepositorySupport = mock()

    var inquiryDetailsRepository: InquiryDetailsRepository = mock()

    var inquiryDetailsRepositorySupport: InquiryDetailsRepositorySupport = mock()

    lateinit var inquiryService: InquiryService

    private lateinit var testInquiry: Inquiry
    private lateinit var testInquiryDetails: InquiryDetails

    @BeforeEach
    fun setUp() {
        inquiryService = InquiryServiceImpl(
            inquiryRepository,
            inquiryRepositorySupport,
            inquiryDetailsRepository,
            inquiryDetailsRepositorySupport
        )
        testInquiry = Inquiry().apply {
            userId = 1
            title = "TITLE"
            createdBy = "고객"
        }
        testInquiryDetails = InquiryDetails().apply {
            inquiryId = 1
            content = "CONTENT"
            createdBy = "상담사1"
        }
    }

    @Test
    fun `test getInquiries`() {
        val searchForm = SearchForm(size = 10, page = 0, inquiryId = null)
        inquiryRepositorySupport.findAll(searchForm)
    }

    @Test
    fun `test getInquiryDetails`() {
        val searchForm = SearchForm(size = 10, page = 0, inquiryId = 1)
        inquiryDetailsRepositorySupport.findDetailsByInquiryId(searchForm)
    }

    @Test
    fun `test createInquiry`() {
        inquiryRepository.save(testInquiry)
        inquiryDetailsRepository.save(testInquiryDetails)
    }

    @Test
    fun `test createInquiryDetails`() {
        testInquiry.assigner = 1
        inquiryRepository.save(testInquiry)
        inquiryDetailsRepository.save(testInquiryDetails)
        testInquiry.status = InquiryStatus.ING
        inquiryRepository.save(testInquiry)
    }

    @Test
    fun `test checkInquiryAssigner and updateInquiryAssigner`() {
        val tokenToUserInfo = TokenToUserInfo(id = 1, name = "상담사1", roles = listOf("ROLE_ADMIN"))
        assert(tokenToUserInfo.roles.contains("ROLE_ADMIN"))
        testInquiry.assigner = 1
        inquiryRepository.save(testInquiry)
        assert(testInquiry.assigner != null && testInquiry.assigner == tokenToUserInfo.id)
    }

    @Test
    fun `test updateInquiryStatus`() {
        val tokenToUserInfo = TokenToUserInfo(id = 1, name = "상담사1", roles = listOf("ROLE_ADMIN"))
        assert(tokenToUserInfo.roles.contains("ROLE_ADMIN"))
        testInquiry.assigner = 1
        inquiryRepository.save(testInquiry)
        assert(testInquiry.assigner != null && testInquiry.assigner == tokenToUserInfo.id)
        testInquiry.status = InquiryStatus.valueOf("END")
        inquiryRepository.save(testInquiry)
        assert(testInquiry.status == InquiryStatus.END)
    }

}