package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.QTransaction;
import au.id.keen.forecast.entity.Transaction;
import au.id.keen.forecast.entity.param.TransactionDaoParam;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.List;

public class TransactionRepositoryCustomImpl extends QuerydslRepositorySupport implements TransactionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public TransactionRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Transaction.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Transaction> find(TransactionDaoParam pParam) {

        JPAQuery<Transaction> query = getJPAQuery(pParam);
        return query.fetch();
    }

    @Override
    public long count(TransactionDaoParam pParam) {
        return getJPAQuery(pParam).fetchCount();
    }

    private JPAQuery<Transaction> getJPAQuery(TransactionDaoParam pParam) {
        QTransaction q = QTransaction.transaction;

        JPAQuery<Transaction> query = jpaQueryFactory.selectFrom(q);

        if (StringUtils.isNotBlank(pParam.getUserEmail())) {
            query.where(q.account.user.email.eq(pParam.getUserEmail()));
        }
        if (pParam.getPage() != null && pParam.getSize() != null) {
            // TODO: confirm this is correct
            query.offset(pParam.getPage() * pParam.getSize());
        }
        if (pParam.getSize() != null) {
            query.limit(pParam.getSize());
        }
        if (CollectionUtils.isNotEmpty(pParam.getAccountNames())) {
            query.where(q.account.name.in(pParam.getAccountNames()));
        }
        if (CollectionUtils.isNotEmpty(pParam.getIds())) {
            query.where(q.id.in(pParam.getIds()));
        }

        // TODO: figure out ordering
        // transactions may fall on same data, so date alone is not good enough for sorting (paging)
        OrderSpecifier<LocalDate> dateDesc = q.date.desc();
        OrderSpecifier<Integer> idDesc = q.id.desc();

        query.orderBy(dateDesc, idDesc);
        return query;
    }
}