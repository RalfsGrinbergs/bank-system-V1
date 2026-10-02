package banksystem.Entity;

import jakarta.persistence.*;

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

    public BankAccount() {
    }

    public BankAccount(User user, Long accountNumber) {
        this.user = user;
        this.accountNumber = accountNumber;
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
}
