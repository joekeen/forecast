package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.TransactionSchedule;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionScheduleRepository
        extends TransactionScheduleRepositoryCustom, CrudRepository<TransactionSchedule, Integer>, QuerydslPredicateExecutor<TransactionSchedule> {
}
