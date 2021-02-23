package com.enzitechnologies.sakinbank.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "deposits")
public class Deposit {

//    deposit id
    @Id
    @Column(name = "deposit_id")
    private String depositId;

//    Account
    private String account_id;

//    type
    private String deposit_type;

//    amount
    private long amount;

//    savings points generated
    private long ssp_generated;

//    Deposit Account

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "account_id"))
    private Account account;

//    date of deposit
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_of_deposit;

    public Deposit() {
    }

    public Deposit(
            String depositId,
            String account_id,
            String deposit_type,
            long amount,
            long ssp_generated,
            Date date_of_deposit,
            Account account
            ) {
        this.depositId = depositId;
        this.account_id = account_id;
        this.deposit_type = deposit_type;
        this.amount = amount;
        this.ssp_generated = ssp_generated;
        this.date_of_deposit = date_of_deposit;
        this.account = account;
    }

    public String getDepositId() {
        return depositId;
    }

    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getDeposit_type() {
        return deposit_type;
    }

    public void setDeposit_type(String deposit_type) {
        this.deposit_type = deposit_type;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getSsp_generated() {
        return ssp_generated;
    }

    public void setSsp_generated(long ssp_generated) {
        this.ssp_generated = ssp_generated;
    }

    public Date getDate_of_deposit() {
        return date_of_deposit;
    }

    public void setDate_of_deposit(Date date_of_deposit) {
        this.date_of_deposit = date_of_deposit;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
