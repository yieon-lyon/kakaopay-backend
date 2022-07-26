package yieon.kakaopay.workplatform.handler.request

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Req - SignUp
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
data class SignUpReq(
    val email: String,
    val password: String,
    val passwordConfirmation: String,
    val name: String,
    val login: String
)