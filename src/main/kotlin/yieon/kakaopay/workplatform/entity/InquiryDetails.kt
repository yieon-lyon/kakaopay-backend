package yieon.kakaopay.workplatform.entity

import com.fasterxml.jackson.annotation.JsonFormat
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
 * summary : Entity - InquiryDetails
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Entity
@SequenceGenerator(name = "inquiry_details_seq_generator", sequenceName = "inquiry_details_seq", initialValue = 1, allocationSize = 1)
@Table(indexes = [Index(columnList = "id"), Index(columnList = "createdAt DESC")])
class InquiryDetails {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inquiry_details_seq_generator")
    var id: Long = 0

    @Column(nullable = false)
    var inquiryId: Long = 0

    var content: String = ""

    @Column(nullable = false)
    var createdBy: String = ""

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(nullable = false)
    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(nullable = false)
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null

}