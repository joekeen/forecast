package au.id.keen.forecast.entity.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PayTransactionScheduleDTO {

    private BigDecimal amount;
    private String note;
    private LocalDate date;
    private List<TagDTO> tags;
    private Integer integerWeeks;
    private Integer integerMonths;

}