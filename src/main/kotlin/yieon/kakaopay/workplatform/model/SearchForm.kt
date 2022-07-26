package yieon.kakaopay.workplatform.model

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : SearchForm
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
data class SearchForm (
    var page: Int,
    var size: Int,
    var inquiryId: Long?
)