package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.TransactionSchedule;
import au.id.keen.forecast.entity.param.TransactionScheduleDaoParam;

import java.util.List;

public interface TransactionScheduleRepositoryCustom {

    List<TransactionSchedule> find(TransactionScheduleDaoParam pParam);
}
