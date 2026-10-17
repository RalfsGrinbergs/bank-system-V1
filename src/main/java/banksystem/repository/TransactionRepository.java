package banksystem.repository;

import banksystem.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findByFromAccount_UserEmailOrToAccount_UserEmail(
            String fromAccountEmail,
            String toAccountEmail
    );
}
