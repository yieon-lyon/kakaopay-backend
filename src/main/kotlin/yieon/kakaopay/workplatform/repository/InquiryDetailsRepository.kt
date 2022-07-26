package yieon.kakaopay.workplatform.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import yieon.kakaopay.workplatform.entity.InquiryDetails
import yieon.kakaopay.workplatform.entity.QInquiryDetails
import yieon.kakaopay.workplatform.model.SearchForm

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : Jpa - InquiryDetailsRepo & QueryDsl
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
@Repository
interface InquiryDetailsRepository : JpaRepository<InquiryDetails, Long>

@Repository
class InquiryDetailsRepositorySupport(
    val queryFactory: JPAQueryFactory
): QuerydslRepositorySupport(InquiryDetails::class.java) {

    fun findDetailsByInquiryId(condition: SearchForm): Page<InquiryDetails> {
        val inquiryDetails: QInquiryDetails = QInquiryDetails.inquiryDetails

        val builder = BooleanBuilder()
        builder.and(inquiryDetails.inquiryId.eq(condition.inquiryId))

        val query = queryFactory.selectFrom(inquiryDetails).where(builder)
        val pageable = PageRequest.of(condition.page, condition.size, Sort.by("createdAt").ascending())
        val contents = querydsl!!.applyPagination(pageable, query).fetchResults()

        return PageImpl(contents.results, pageable, contents.total)
    }
}
