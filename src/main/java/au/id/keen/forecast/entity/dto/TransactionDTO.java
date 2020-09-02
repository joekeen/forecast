package au.id.keen.forecast.entity.dto;

import au.id.keen.forecast.entity.Transaction;
import au.id.keen.forecast.entity.TransactionSchedule;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class TransactionDTO {

    private AccountDTO account;
    private BigDecimal amount;
    private BigDecimal balance;
    private String note;
    private LocalDate date; // startDate for schedule
    private LocalDate endDate; // only for schedule
    private Integer id;
    private List<TagDTO> tags;
    private Boolean spreadWeeks; // TODO: rename intervalWeeks
    private Boolean spreadMonths; // TODO: rename intervalMonths
    private Integer spread; // TODO: rename interval
    private boolean showInterval;
    private boolean isSchedule;

    public TransactionDTO() {
    }

    public TransactionDTO(TransactionSchedule pTransactionSchedule) {
        this.isSchedule = true;
        this.id = pTransactionSchedule.getId();
        this.date = pTransactionSchedule.getStartDate();
        this.endDate = pTransactionSchedule.getEndDate();
        this.account = new AccountDTO(pTransactionSchedule.getAccount());
        if (CollectionUtils.isNotEmpty(pTransactionSchedule.getTags())) {
            this.tags = pTransactionSchedule.getTags().stream().map(TagDTO::new).sorted().collect(Collectors.toList());
        } else {
            this.tags = Collections.emptyList();
        }
        this.note = pTransactionSchedule.getNote();
        this.amount = pTransactionSchedule.getAmount();
        this.spreadWeeks = pTransactionSchedule.getIntervalWeeks() > 0 || pTransactionSchedule.getIntervalMonths() == 0;
        this.spreadMonths = pTransactionSchedule.getIntervalMonths() > 0;
        this.spread = pTransactionSchedule.getIntervalWeeks() > 0
                ? pTransactionSchedule.getIntervalWeeks() : pTransactionSchedule.getIntervalMonths();
    }

    public TransactionDTO(Transaction pTransaction) {
        this.id = pTransaction.getId();
        this.date = pTransaction.getDate();
        this.account = new AccountDTO(pTransaction.getAccount());
        if (CollectionUtils.isNotEmpty(pTransaction.getTags())) {
            this.tags = pTransaction.getTags().stream().map(TagDTO::new).sorted().collect(Collectors.toList());
        } else {
            this.tags = Collections.emptyList();
        }
        this.note = pTransaction.getNote();
        this.amount = pTransaction.getAmount();
        this.spreadWeeks = Optional.ofNullable(pTransaction.getIntervalWeeks()).orElse(0) > 0 || Optional.ofNullable(pTransaction.getIntervalMonths()).orElse(0) == 0;
        this.spreadMonths = Optional.ofNullable(pTransaction.getIntervalMonths()).orElse(0) > 0;
        this.spread = Optional.ofNullable(pTransaction.getIntervalWeeks()).orElse(0) > 0
                ? Optional.ofNullable(pTransaction.getIntervalWeeks()).orElse(0) : Optional.ofNullable(pTransaction.getIntervalMonths()).orElse(0);
    }

}