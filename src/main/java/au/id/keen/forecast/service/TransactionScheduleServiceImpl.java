package au.id.keen.forecast.service;

import au.id.keen.forecast.entity.Account;
import au.id.keen.forecast.entity.Tag;
import au.id.keen.forecast.entity.Transaction;
import au.id.keen.forecast.entity.TransactionSchedule;
import au.id.keen.forecast.entity.dto.AccountDTO;
import au.id.keen.forecast.entity.dto.AddTransactionScheduleDTO;
import au.id.keen.forecast.entity.dto.EditTransactionScheduleDTO;
import au.id.keen.forecast.entity.dto.PayTransactionScheduleDTO;
import au.id.keen.forecast.entity.dto.TagDTO;
import au.id.keen.forecast.entity.dto.TransactionScheduleDTO;
import au.id.keen.forecast.entity.param.AccountDaoParam;
import au.id.keen.forecast.entity.param.TagDaoParam;
import au.id.keen.forecast.entity.param.TransactionScheduleDaoParam;
import au.id.keen.forecast.repository.TransactionScheduleRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionScheduleServiceImpl implements TransactionScheduleService {

    private final TransactionScheduleRepository transactionScheduleRepository;
    private final TransactionService transactionService;
    private final TagService tagService;
    private final AccountService accountService;

    public TransactionScheduleServiceImpl(TransactionScheduleRepository transactionScheduleRepository, TransactionService transactionService, TagService tagService, AccountService accountService) {
        this.transactionScheduleRepository = transactionScheduleRepository;
        this.transactionService = transactionService;
        this.tagService = tagService;
        this.accountService = accountService;
    }

    @Override
    public List<Transaction> getTransactionsForAccount(Account pAccount, LocalDate pToDate) {
        TransactionScheduleDaoParam param = new TransactionScheduleDaoParam();
        param.setAccount(pAccount);
        List<TransactionSchedule> schedules = transactionScheduleRepository.find(param);

        List<Transaction> transactions = Lists.newArrayList();

        for (TransactionSchedule schedule : schedules) {
            LocalDate date = schedule.getStartDate();
            while ((date.isBefore(pToDate) || date.isEqual(pToDate)) && (schedule.getEndDate() == null || date.isBefore(schedule.getEndDate()))) {
                Transaction t = new Transaction(schedule, date);
                transactions.add(t);
                date = getNextStartDate(date, schedule.getIntervalWeeks(), schedule.getIntervalMonths());
            }
        }

        return transactions;
    }

    @Override
    public List<TransactionScheduleDTO> find(TransactionScheduleDaoParam pParam) {
        // TODO: default sorting? to prevent NPE on getComparator()
        return transactionScheduleRepository.find(pParam).stream().map(TransactionScheduleDTO::new).collect(Collectors.toList());
    }

    @Override
    public void payTransactionSchedule(TransactionScheduleDaoParam pParam, PayTransactionScheduleDTO pDTO) {
        List<TransactionSchedule> schedules = transactionScheduleRepository.find(pParam);

        // TODO: check we only find one?

        List<Transaction> transactions = Lists.newArrayList();
        for (TransactionSchedule schedule : schedules) {
            Transaction transaction = new Transaction(schedule, schedule.getStartDate());
            // override date if required from client
            if (null != pDTO.getDate()) {
                transaction.setDate(pDTO.getDate());
            }
            // other from client
            transaction.setNote(pDTO.getNote());
            transaction.setAmount(pDTO.getAmount());
            transaction.setTags(Sets.newHashSet(getTags(pParam.getUserEmail(), pDTO)));

            transaction.setIntervalWeeks(pDTO.getIntegerWeeks());
            transaction.setIntervalMonths(pDTO.getIntegerMonths());

            transactions.add(transaction);

            // schedule is managed entity, hibernate will persist changes
            schedule.setStartDate(getNextStartDate(schedule.getStartDate(), schedule.getIntervalWeeks(), schedule.getIntervalMonths()));

            if (schedule.getEndDate() == null) {
                continue;
            }

            if (schedule.getStartDate().isEqual(schedule.getEndDate())
                    || schedule.getStartDate().isAfter(schedule.getEndDate())) {
                transactionScheduleRepository.delete(schedule);
            }
        }

        transactionService.save(transactions);
    }

    @Override
    public void editTransactionSchedule(TransactionScheduleDaoParam pParam, EditTransactionScheduleDTO pDTO) {
        List<TransactionSchedule> schedules = transactionScheduleRepository.find(pParam);

        // check only one

        TransactionSchedule schedule = schedules.get(0);

        String userEmail = pParam.getUserEmail();
        setProperties(schedule, userEmail, pDTO.getDate(), pDTO.getEndDate(), pDTO.getAmount(), pDTO.getAccount(),
                getTags(userEmail, pDTO), pDTO.getNote(), pDTO.getIntegerWeeks(), pDTO.getIntegerMonths());

    }

    private void setProperties(TransactionSchedule schedule, String userEmail, LocalDate date, LocalDate endDate,
                               BigDecimal amount, AccountDTO account, List<Tag> tags, String note, Integer spreadWeeks,
                               Integer spreadMonths) {
        schedule.setStartDate(date);
        schedule.setEndDate(endDate);
        schedule.setAmount(amount);
        schedule.setAccount(getAccount(userEmail, account));
        schedule.setTags(Sets.newHashSet(tags)); // TODO: confirm this works as expected
        schedule.setNote(note);
        schedule.setIntervalWeeks(spreadWeeks);
        schedule.setIntervalMonths(spreadMonths);
    }

    @Override
    public void addTransactionSchedule(String pUserEmail, AddTransactionScheduleDTO pDTO) {

        TransactionSchedule schedule = new TransactionSchedule();

        setProperties(schedule, pUserEmail, pDTO.getDate(), pDTO.getEndDate(), pDTO.getAmount(), pDTO.getAccount(),
                getTags(pUserEmail, pDTO), pDTO.getNote(), pDTO.getIntegerWeeks(), pDTO.getIntegerMonths());

        transactionScheduleRepository.save(schedule);
    }

    private Account getAccount(String pUserEmail, AccountDTO pDTO) {
        AccountDaoParam param = new AccountDaoParam();
        param.setUserEmail(pUserEmail);
        param.setName(pDTO.getName());
//        return accountService.findOne(param);
        // TODO: fix me
        return null;
    }

    private LocalDate getNextStartDate(LocalDate pStartDate, Integer pIntervalWeeks, Integer pIntervalMonths) {
        if (pIntervalWeeks > 0) {
            return pStartDate.plusWeeks(pIntervalWeeks);
        } else if (pIntervalMonths > 0) {
            return pStartDate.plusMonths(pIntervalMonths);
        }
        throw new UnsupportedOperationException();
    }

    private List<Tag> getTags(String pUserEmail, PayTransactionScheduleDTO pDTO) {
        TagDaoParam param = new TagDaoParam();
        param.setUserEmail(pUserEmail);
        param.setNames(pDTO.getTags().stream().map(TagDTO::getName).collect(Collectors.toSet()));
        // TODO: fix me
//        return tagService.find(param);
        return null;
    }
}
