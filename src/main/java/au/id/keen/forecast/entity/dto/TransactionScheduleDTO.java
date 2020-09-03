package au.id.keen.forecast.entity.dto;

import au.id.keen.forecast.entity.TransactionSchedule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionScheduleDTO extends TransactionDTO {

    private LocalDate endDate;

    public TransactionScheduleDTO(TransactionSchedule pTransactionSchedule) {
        setId(pTransactionSchedule.getId());
        setDate(pTransactionSchedule.getStartDate());
        this.endDate = pTransactionSchedule.getEndDate();
        setAccount(new AccountDTO(pTransactionSchedule.getAccount()));
        if (CollectionUtils.isNotEmpty(pTransactionSchedule.getTags())) {
            setTags(pTransactionSchedule.getTags().stream().map(TagDTO::new).sorted().collect(Collectors.toList()));
        } else {
            setTags(Collections.emptyList());
        }
        setNote(pTransactionSchedule.getNote());
        setAmount(pTransactionSchedule.getAmount());
        setIntervalWeeks(pTransactionSchedule.getIntervalWeeks() > 0 || pTransactionSchedule.getIntervalMonths() == 0);
        setIntervalMonths(pTransactionSchedule.getIntervalMonths() > 0);
        setInterval(pTransactionSchedule.getIntervalWeeks() > 0
                ? pTransactionSchedule.getIntervalWeeks() : pTransactionSchedule.getIntervalMonths());
    }

    public List<String> getTagsNames() {
        return this.getTags().stream().map(TagDTO::getName).sorted().collect(Collectors.toList());
    }
}