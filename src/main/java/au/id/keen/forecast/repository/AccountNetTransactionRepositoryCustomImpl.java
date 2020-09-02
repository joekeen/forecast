package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.Account;
import au.id.keen.forecast.entity.AccountNetTransaction;
import au.id.keen.forecast.entity.QAccountNetTransaction;
import au.id.keen.forecast.entity.param.AccountNetTransactionDaoParam;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AccountNetTransactionRepositoryCustomImpl extends QuerydslRepositorySupport implements AccountNetTransactionRepositoryCustom {

    public static final QAccountNetTransaction ANT = QAccountNetTransaction.accountNetTransaction;
    private final JPAQueryFactory jpaQueryFactory;

    public AccountNetTransactionRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(AccountNetTransaction.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public BigDecimal getNetTransactionAmountTo(Account pAccount, LocalDate pToDate) {
        List<BigDecimal> list = jpaQueryFactory.select(ANT.netAmount.sum())
                .from(ANT)
                .where(ANT.account.eq(pAccount))
                .where(ANT.date.loe(pToDate))
                .fetch();
        return list.get(0) == null ? BigDecimal.ZERO : list.get(0);
    }

    @Override
    public List<AccountNetTransaction> find(AccountNetTransactionDaoParam pParam) {
        JPAQuery<AccountNetTransaction> query = jpaQueryFactory.selectFrom(ANT);

        if (pParam.getAccount() != null) {
            query.where(ANT.account.eq(pParam.getAccount()));
        }
        if (pParam.getFromDateInc() != null) {
            if (pParam.getToDateInc() != null) {
                query.where(ANT.date.between(pParam.getFromDateInc(), pParam.getToDateInc()));
            } else {
                query.where(ANT.date.goe(pParam.getFromDateInc()));
            }
        }
        if (pParam.getToDateInc() != null
                && pParam.getFromDateInc() == null) {
            query.where(ANT.date.loe(pParam.getToDateInc()));
        }

        return query.fetch();
    }

}