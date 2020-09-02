package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.AccountInterestRate;
import au.id.keen.forecast.entity.QTransactionSchedule;
import au.id.keen.forecast.entity.TransactionSchedule;
import au.id.keen.forecast.entity.param.TransactionScheduleDaoParam;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class TransactionScheduleRepositoryCustomImpl extends QuerydslRepositorySupport implements TransactionScheduleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public TransactionScheduleRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(AccountInterestRate.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<TransactionSchedule> find(TransactionScheduleDaoParam pParam) {

        QTransactionSchedule q = QTransactionSchedule.transactionSchedule;

        JPAQuery<TransactionSchedule> query = jpaQueryFactory.selectFrom(q);

        if (pParam.getAccount() != null) {
            query.where(q.account.eq(pParam.getAccount()));
        }
        if (StringUtils.isNotBlank(pParam.getUserEmail())) {
            query.where(q.account.user.email.eq(pParam.getUserEmail()));
        }
        if (CollectionUtils.isNotEmpty(pParam.getIds())) {
            query.where(q.id.in(pParam.getIds()));
        }
        if (pParam.getToDate() != null) {
            query.where(q.startDate.loe(pParam.getToDate()));
        }

        // TODO: ordering
        // https://stackoverflow.com/questions/20090098/how-do-you-specify-multi-column-orderspecifier-for-use-in-springdata-and-queryds
        query.orderBy(q.startDate.asc());

        return query.fetch();
    }
}
