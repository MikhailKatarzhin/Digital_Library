package library.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "\"Wallet\"")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Wallet {
    @Id
    private Long id;

    @Column(nullable = false)
    private Long balance = 0L;


    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "id", nullable = false)
    private User user;


    public boolean debitingFunds(Long requiredFunds) {
        if (requiredFunds < 0) throw new IllegalArgumentException("Required funds must be zero or positive");
        if (balance < requiredFunds) return false;
        balance -= requiredFunds;
        return true;
    }

    public Long replenishmentFunds(Long incomingFunds) {
        if (incomingFunds < 1) throw new IllegalArgumentException("Incoming funds must be positive");
        balance += incomingFunds;
        return balance;
    }
}
