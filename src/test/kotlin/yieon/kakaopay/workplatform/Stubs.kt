package yieon.kakaopay.workplatform

import yieon.kakaopay.workplatform.entity.Inquiry
import yieon.kakaopay.workplatform.entity.InquiryDetails
import yieon.kakaopay.workplatform.entity.User

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Test - Stub
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
object Stubs {
    val testUser = User().apply {
        email = "test@test.com"
        password = "password"
        name = "카카오"
        login = "test"
    }

    val testInquiry = Inquiry().apply {
        userId = 1
        title = "TITLE"
        createdBy = "고객"
    }

    val testInquiryDetails = InquiryDetails().apply {
        inquiryId = 1
        content = "CONTENT"
        createdBy = "상담사1"
    }

}