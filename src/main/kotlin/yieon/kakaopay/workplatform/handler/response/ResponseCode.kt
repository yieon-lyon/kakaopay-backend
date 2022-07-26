package yieon.kakaopay.workplatform.handler.response

import com.fasterxml.jackson.annotation.JsonFormat

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Res - CodeList
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ResponseCode(val code: Int, val msg: String) {
    SUCCESS(0, ""),
    INVALID_CREDENTIALS(1001, "이메일 혹은 비밀번호를 다시 확인해주세요."),
    TOO_SHORT_PASSWORD(1002, "비밀번호를 6자 이상 설정해주세요."),
    ALREADY_EXIST_EMAIL(1003, "이미 사용중인 이메일입니다."),
    NOT_SELECTED_ASSIGNER(1004, "담당자가 지정되지 않은 문의이거나 존재하지 않는 문의입니다."),
    ALREADY_ASSIGNER(1005, "이미 담당자가 지정된 문의건입니다."),
    API_PERMISSION_DENIED(1006, "서비스 접근 권한이 존재하지 않습니다."),
}