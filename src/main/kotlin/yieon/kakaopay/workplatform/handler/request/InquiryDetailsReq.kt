package yieon.kakaopay.workplatform.handler.request

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Req - InquiryDetails
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
data class InquiryDetailsReq(
    val inquiryId: Long,
    val content: String
)