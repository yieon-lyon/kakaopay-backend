package yieon.kakaopay.workplatform.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import yieon.kakaopay.workplatform.entity.enum.InquiryStatus
import java.time.LocalDateTime
import javax.persistence.*

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Entity - Inquiry
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Entity
@SequenceGenerator(name = "inquiry_seq_generator", sequenceName = "inquiry_seq", initialValue = 1, allocationSize = 1)
@Table(indexes = [Index(columnList = "id"), Index(columnList = "createdAt DESC")])
class Inquiry {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inquiry_seq_generator")
    var id: Long = 0

    @Column(nullable = false)
    var userId: Long = 0

    @Column(nullable = false)
    var title: String = ""

    @Column(nullable = false)
    var createdBy: String = ""

    var assigner: Long? = null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: InquiryStatus = InquiryStatus.OPEN

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(nullable = false)
    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(nullable = false)
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="inquiryId")
    var inquiryDetails: MutableSet<InquiryDetails> = mutableSetOf()
}