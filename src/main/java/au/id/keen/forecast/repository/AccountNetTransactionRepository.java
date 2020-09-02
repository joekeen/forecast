package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.AccountNetTransaction;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountNetTransactionRepository extends AccountNetTransactionRepositoryCustom, CrudRepository<AccountNetTransaction, Integer>, QuerydslPredicateExecutor<AccountNetTransaction> {
}