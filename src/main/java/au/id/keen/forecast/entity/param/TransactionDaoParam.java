package au.id.keen.forecast.entity.param;

import lombok.Data;

import java.util.Set;

@Data
public class TransactionDaoParam extends AbstractBaseDaoParam {

    private Set<String> accountNames;
    private Set<Integer> ids;

    private boolean includeBalance;
}