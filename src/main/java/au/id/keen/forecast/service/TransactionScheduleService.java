package au.id.keen.forecast.service;

import au.id.keen.forecast.entity.Account;
import au.id.keen.forecast.entity.Transaction;
import au.id.keen.forecast.entity.TransactionSchedule;
import au.id.keen.forecast.entity.dto.AddTransactionScheduleDTO;
import au.id.keen.forecast.entity.dto.EditTransactionScheduleDTO;
import au.id.keen.forecast.entity.dto.PayTransactionScheduleDTO;
import au.id.keen.forecast.entity.dto.TransactionScheduleDTO;
import au.id.keen.forecast.entity.param.TransactionScheduleDaoParam;

import java.time.LocalDate;
import java.util.List;

public interface TransactionScheduleService {

    List<Transaction> getTransactionsForAccount(Account pAccount, LocalDate pToDate);

    List<TransactionScheduleDTO> find(TransactionScheduleDaoParam pParam);

    void payTransactionSchedule(TransactionScheduleDaoParam pParam, PayTransactionScheduleDTO pDTO);

    void editTransactionSchedule(TransactionScheduleDaoParam pParam, EditTransactionScheduleDTO pDTO);

    void addTransactionSchedule(String pUserEmail, AddTransactionScheduleDTO pDTO);
}
