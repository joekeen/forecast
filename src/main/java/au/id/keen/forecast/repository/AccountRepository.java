package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.Account;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends AccountRepositoryCustom, CrudRepository<Account, Integer>, QuerydslPredicateExecutor<Account> {
}