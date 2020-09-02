package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.Transaction;
import au.id.keen.forecast.entity.param.TransactionDaoParam;

import java.util.List;

public interface TransactionRepositoryCustom {

    List<Transaction> find(TransactionDaoParam pParam);

    long count(TransactionDaoParam pParam);
}