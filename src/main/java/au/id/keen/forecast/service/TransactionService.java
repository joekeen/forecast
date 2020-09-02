package au.id.keen.forecast.service;

import au.id.keen.forecast.entity.dto.TransactionDTO;
import au.id.keen.forecast.entity.param.TransactionDaoParam;

import java.util.List;

public interface TransactionService {

    List<TransactionDTO> find(TransactionDaoParam pParam);

    long count(TransactionDaoParam pParam);
}