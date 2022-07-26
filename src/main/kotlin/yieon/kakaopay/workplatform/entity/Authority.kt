package yieon.kakaopay.workplatform.entity

import yieon.kakaopay.workplatform.entity.enum.Role
import javax.persistence.*

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Entity - Authority
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Entity
@Table
class Authority {

    @javax.persistence.Id
    @Enumerated(EnumType.STRING)
    var name: Role = Role.ROLE_USER

}
