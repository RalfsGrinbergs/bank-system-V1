package banksystem.Entity;

import banksystem.Enum.TransactionsType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionsType type;

    private BigDecimal amount;

    private LocalDateTime timestamp;

    @ManyToOne
    private BankAccount fromAccount;

    @ManyToOne
    private BankAccount toAccount;
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Transactions() {
    }

    public Transactions(BankAccount toAccount, BankAccount fromAccount, LocalDateTime timestamp, BigDecimal amount, TransactionsType type) {
        this.toAccount = toAccount;
        this.fromAccount = fromAccount;
        this.timestamp = timestamp;
        this.amount = amount;
        this.type = type;
    }

    public TransactionsType getType() {
        return type;
    }

    public void setType(TransactionsType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public BankAccount getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(BankAccount fromAccount) {
        this.fromAccount = fromAccount;
    }

    public BankAccount getToAccount() {
        return toAccount;
    }

    public void setToAccount(BankAccount toAccount) {
        this.toAccount = toAccount;
    }
}
