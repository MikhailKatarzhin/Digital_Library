package library.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "\"Wallet\"")
public class Wallet {

    @Column(name = "balance", nullable = false)
    private Long balance;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
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