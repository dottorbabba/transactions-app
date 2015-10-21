package com.bandera.transactions_app;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
		
	@Query("SELECT id FROM Transaction WHERE type = :type")
	List<Long> findByType(@Param("type") String type);

	@Query("SELECT SUM(amount) FROM Transaction WHERE path LIKE :id||'/%' OR path LIKE '%/'||:id||'/%'")
	Double sumDescendantsAmount(@Param("id") long id);
}
