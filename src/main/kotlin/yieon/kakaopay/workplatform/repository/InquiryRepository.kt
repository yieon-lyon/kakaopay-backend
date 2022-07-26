package yieon.kakaopay.workplatform.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import yieon.kakaopay.workplatform.entity.Inquiry
import yieon.kakaopay.workplatform.entity.QInquiry
import yieon.kakaopay.workplatform.model.SearchForm

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Jpa - InquiryRepo & QueryDsl
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Repository
interface InquiryRepository : JpaRepository<Inquiry, Long>

@Repository
class InquiryRepositorySupport(
    val queryFactory: JPAQueryFactory
): QuerydslRepositorySupport(Inquiry::class.java) {

    fun findAll(condition: SearchForm): Page<Inquiry> {
        val inquiry: QInquiry = QInquiry.inquiry
        val query = queryFactory.selectFrom(inquiry)
        val pageable = PageRequest.of(condition.page, condition.size, Sort.by("createdAt").descending())
        val contents = querydsl!!.applyPagination(pageable, query).fetchResults()

        return PageImpl(contents.results, pageable, contents.total)
    }
}