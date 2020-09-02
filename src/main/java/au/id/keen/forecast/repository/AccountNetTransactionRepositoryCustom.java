package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.Account;
import au.id.keen.forecast.entity.AccountNetTransaction;
import au.id.keen.forecast.entity.param.AccountNetTransactionDaoParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AccountNetTransactionRepositoryCustom {
    BigDecimal getNetTransactionAmountTo(Account pAccount, LocalDate pToDate);

    List<AccountNetTransaction> find(AccountNetTransactionDaoParam pParam);
}