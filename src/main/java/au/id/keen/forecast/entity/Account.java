package au.id.keen.forecast.entity;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Version;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Entity
@Data
@ToString(exclude = {"user", "offsetAccounts"})
@EqualsAndHashCode(exclude = {"interestRates", "user", "offsetAccounts", "balance"})
@QueryEntity
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal openingBalance;

    private LocalDate openingBalanceDate;

    private LocalDateTime createdAt;

    @Column(length = 30)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private Set<AccountInterestRate> interestRates;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "account_offset", joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "offset_account_id", referencedColumnName = "id"))
    @OrderBy("name")
    @Getter(AccessLevel.PRIVATE)
    private Set<Account> offsetAccounts;

    public Optional<Account> getOffsetAccount() {
        return getOffsetAccounts() == null ? Optional.empty() : offsetAccounts.stream().findFirst();
    }

    @OneToOne(mappedBy = "account")
    private AccountBalance balance;

    @Version
    private Integer version;
}