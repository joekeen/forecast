package au.id.keen.forecast.entity.param;

import au.id.keen.forecast.entity.Account;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class TransactionScheduleDaoParam extends AbstractBaseDaoParam {

    private Account account;
    private Set<Integer> ids;
    private LocalDate toDate; // inclusive

}