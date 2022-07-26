package yieon.kakaopay.workplatform.handler.request

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Req - Inquiry
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
data class InquiryReq(
    val title: String,
    val content: String,
    val createdBy: String
)