package yieon.kakaopay.workplatform.model

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : TokenToUserInfo
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
data class TokenToUserInfo (
    var id: Long,
    var name: String,
    var roles: List<String>
)