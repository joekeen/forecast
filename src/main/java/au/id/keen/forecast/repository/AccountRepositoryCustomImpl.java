package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.Account;
import au.id.keen.forecast.entity.QAccount;
import au.id.keen.forecast.entity.QAccountNetTransaction;
import au.id.keen.forecast.entity.param.AccountDaoParam;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class AccountRepositoryCustomImpl extends QuerydslRepositorySupport implements AccountRepositoryCustom {

    public static final QAccountNetTransaction ANT = QAccountNetTransaction.accountNetTransaction;
    private final JPAQueryFactory jpaQueryFactory;

    public AccountRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Account.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Account> find(AccountDaoParam pParam) {
        QAccount account = QAccount.account;

        JPAQuery<Account> query = jpaQueryFactory.selectFrom(account);

        if (StringUtils.isNotBlank(pParam.getUserEmail())) {
            query.where(account.user.email.eq(pParam.getUserEmail()));
        }
        if (StringUtils.isNotBlank(pParam.getName())) {
            query.where(account.name.eq(pParam.getName()));
        }

        return query.fetch();

    }
}