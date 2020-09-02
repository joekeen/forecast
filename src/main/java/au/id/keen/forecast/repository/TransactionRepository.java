package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.Transaction;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends TransactionRepositoryCustom, CrudRepository<Transaction, Integer>, QuerydslPredicateExecutor<Transaction> {
}