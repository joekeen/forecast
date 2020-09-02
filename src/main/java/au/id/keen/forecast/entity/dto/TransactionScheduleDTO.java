package au.id.keen.forecast.entity.dto;

import au.id.keen.forecast.entity.TransactionSchedule;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TransactionScheduleDTO {

    private AccountDTO account;
    private BigDecimal amount;
    private BigDecimal balance;
    private String note;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer id;
    private List<TagDTO> tags;
    private Boolean spreadWeeks; // TODO: rename intervalWeeks
    private Boolean spreadMonths; // TODO: rename intervalMonths
    private Integer spread; // TODO: rename interval

    public TransactionScheduleDTO(TransactionSchedule pTransactionSchedule) {
        this.id = pTransactionSchedule.getId();
        this.startDate = pTransactionSchedule.getStartDate();
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

    public List<String> getTagsNames() {
        return this.getTags().stream().map(TagDTO::getName).sorted().collect(Collectors.toList());
    }
}