package au.id.keen.forecast.service;

import au.id.keen.forecast.entity.Account;
import au.id.keen.forecast.entity.Transaction;
import au.id.keen.forecast.entity.dto.TransactionDTO;
import au.id.keen.forecast.entity.param.TransactionDaoParam;
import au.id.keen.forecast.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Override
    public List<TransactionDTO> find(TransactionDaoParam pParam) {

        /*
        // check only one account - only support one account for now
        Set<String> accountNames = pList.stream().map(t -> t.getAccount().getName()).collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(accountNames) || CollectionUtils.size(accountNames) > 1) {
            throw new UnsupportedOperationException();
        }
         */

        List<Transaction> list = transactionRepository.find(pParam);
        List<TransactionDTO> dtos = list.stream().map(TransactionDTO::new).collect(Collectors.toList());

        if (pParam.isIncludeBalance()) {

            LocalDate endDate = list.stream().map(Transaction::getDate).max(LocalDate::compareTo).orElse(null);

            Account account = list.get(0).getAccount();

            final BigDecimal[] balanceAt = {accountService.getBalanceAt(account, endDate)};

            dtos.forEach(t -> {
                t.setBalance(balanceAt[0]);
                balanceAt[0] = balanceAt[0].subtract(t.getAmount());
            });
        }

        return dtos;
    }

    @Override
    public long count(TransactionDaoParam pParam) {
        return transactionRepository.count(pParam);
    }
}