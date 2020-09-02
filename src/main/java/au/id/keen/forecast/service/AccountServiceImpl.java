package au.id.keen.forecast.service;

import au.id.keen.forecast.entity.Account;
import au.id.keen.forecast.entity.dto.AccountDTO;
import au.id.keen.forecast.entity.param.AccountDaoParam;
import au.id.keen.forecast.repository.AccountNetTransactionRepository;
import au.id.keen.forecast.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountNetTransactionRepository accountNetTransactionRepository;

    public AccountServiceImpl(AccountRepository accountRepository, AccountNetTransactionRepository accountNetTransactionRepository) {
        this.accountRepository = accountRepository;
        this.accountNetTransactionRepository = accountNetTransactionRepository;
    }

    @Override
    public List<AccountDTO> find(AccountDaoParam pParam) {
        return accountRepository.find(pParam).stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @Override
    public BigDecimal getBalanceAt(Account pAccount, LocalDate pDate) {

        BigDecimal accountNetTransaction = accountNetTransactionRepository.getNetTransactionAmountTo(pAccount, pDate);

        return pAccount.getOpeningBalance().add(accountNetTransaction);
    }


}