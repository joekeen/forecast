package au.id.keen.forecast.entity.param;

import au.id.keen.forecast.entity.Account;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountNetTransactionDaoParam {

    private Account account;
    private LocalDate fromDateInc;
    private LocalDate toDateInc;

}