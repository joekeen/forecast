package au.id.keen.forecast.entity.dto;

import au.id.keen.forecast.entity.Account;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
public class AccountDTO {

    private Integer id;
    private String name;
    private BigDecimal balance;
    @Getter(AccessLevel.NONE)
    private BigDecimal offsetBalance;

    public AccountDTO(Account pAccount) {
        this.id = pAccount.getId();
        this.name = pAccount.getName();
        if (pAccount.getBalance() != null) {
            this.balance = pAccount.getBalance().getBalance();
        }
        pAccount.getOffsetAccount().ifPresent(offset -> {
            this.offsetBalance = offset.getBalance().getBalance();
        });
    }

}