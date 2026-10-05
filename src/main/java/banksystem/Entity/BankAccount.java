package banksystem.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bank_Accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long accountNumber;
    @OneToOne
    @JoinColumn(name = "UserId", referencedColumnName = "id")
    private User user;
    private BigDecimal balance;
    public BankAccount() {
    }

    public BankAccount(User user, Long accountNumber) {
        this.user = user;
        this.accountNumber = accountNumber;
        this.balance = BigDecimal.ZERO;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }



    public void deposit(BigDecimal sum) {
        if (sum.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Sum is 0 or below");
        }

        this.balance = this.balance.add(sum);
    }


}
