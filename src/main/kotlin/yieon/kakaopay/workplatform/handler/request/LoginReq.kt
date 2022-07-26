package yieon.kakaopay.workplatform.handler.request

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Req - Login
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
data class LoginReq(val login: String, val password: String)