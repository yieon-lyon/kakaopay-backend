package yieon.kakaopay.workplatform.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import yieon.kakaopay.workplatform.entity.User

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Jpa - UserRepo
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByLogin(login: String): User?
    fun findAllByEmail(email: String): List<User>
}
