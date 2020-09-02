package au.id.keen.forecast.service;

import au.id.keen.forecast.entity.Account;
import au.id.keen.forecast.entity.dto.AccountDTO;
import au.id.keen.forecast.entity.param.AccountDaoParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AccountService {

    List<AccountDTO> find(AccountDaoParam param);

    BigDecimal getBalanceAt(Account pAccount, LocalDate pDate);
}