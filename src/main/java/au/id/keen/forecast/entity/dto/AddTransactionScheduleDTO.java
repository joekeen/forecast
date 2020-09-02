package au.id.keen.forecast.entity.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AddTransactionScheduleDTO extends PayTransactionScheduleDTO {

    private LocalDate endDate;
    private AccountDTO account;

}