package yieon.kakaopay.workplatform.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Entity - User
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Entity
@SequenceGenerator(name = "user_seq_generator", sequenceName = "user_seq", initialValue = 1, allocationSize = 1)
@Table(indexes = [Index(columnList = "email"), Index(columnList = "updatedAt DESC")])
class User {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_generator")
    var id: Long = 0

    var login: String? = null

    @JsonIgnore
    var password: String? = null

    var name: String? = null

    var email: String? = null

    var activated: Boolean = false

    @Column(nullable = false)
    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @Column(nullable = false)
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null

    var lastSignAt: LocalDateTime? = null

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_authority",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "authority_name", referencedColumnName = "name")]
    )
    var authorities: MutableSet<Authority> = mutableSetOf()

}
