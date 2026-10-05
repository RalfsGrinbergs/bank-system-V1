package banksystem.Repository;

import banksystem.Entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findByFromAccountIdOrToAccountId(Long fromAccountId, Long toAccountId);


}