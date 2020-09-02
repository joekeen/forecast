package au.id.keen.forecast.entity.dto;

import au.id.keen.forecast.entity.Transaction;
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
    private LocalDate date;
    private Integer id;
    private List<TagDTO> tags;
    private Boolean spreadWeeks; // TODO: rename intervalWeeks
    private Boolean spreadMonths; // TODO: rename intervalMonths
    private Integer spread; // TODO: rename interval

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

    public List<String> getTagsNames() {
        return this.getTags().stream().map(TagDTO::getName).sorted().collect(Collectors.toList());
    }
}