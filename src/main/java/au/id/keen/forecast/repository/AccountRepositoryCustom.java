package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.Account;
import au.id.keen.forecast.entity.param.AccountDaoParam;

import java.util.List;

public interface AccountRepositoryCustom {

    List<Account> find(AccountDaoParam pParam);
}