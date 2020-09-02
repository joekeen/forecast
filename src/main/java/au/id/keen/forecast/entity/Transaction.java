package au.id.keen.forecast.entity;

import com.google.common.collect.Sets;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Version;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@ToString(exclude = "account")
public class Transaction {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private BigDecimal amount;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, referencedColumnName = "id")
    private Account account;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "transaction_tag", joinColumns = @JoinColumn(name = "transaction_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    @OrderBy("name")
    private Set<Tag> tags;

    @Column(length = 255)
    private String note;

    private boolean interestInd;
    private Integer intervalWeeks;
    private Integer intervalMonths;

    @Version
    private Integer version;

    public Transaction(TransactionSchedule pSchedule, LocalDate pDate) {
        this.amount = pSchedule.getAmount();
        this.date = pDate;
        this.account = pSchedule.getAccount();
        this.tags = Sets.newHashSet(pSchedule.getTags());
        this.note = pSchedule.getNote();
        this.intervalWeeks = pSchedule.getIntervalWeeks();
        this.intervalMonths = pSchedule.getIntervalMonths();
    }

    public Transaction() {
    }
}