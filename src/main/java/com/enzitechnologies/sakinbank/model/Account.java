package com.enzitechnologies.sakinbank.model;

import javax.persistence.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "account_id")
    private String accountId;

    @Column(name = "user_name")
    private String username;

    private String private_key;

    private String public_key;

    private long account_balance;

    private long SSP_Balance;

    private boolean isDeleted;

    private boolean isAssociated;

    private Instant expirationTime;

    private Duration autoRenewPeriod;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "account"
    )
    private List<Deposit> deposits;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "account"
    )
    private List<Loan> loans;

    public Account() {
    }

    public Account(
            String account_id,
            String username,
            String private_key,
            String public_key,
            long account_balance,
            boolean isDeleted,
            boolean isAssociated,
            Instant expirationTime,
            Duration autoRenewPeriod,
            List<Deposit> deposits,
            List<Loan> loans

    ) {

        this.accountId = account_id;
        this.username = username;
        this.private_key = private_key;
        this.public_key = public_key;
        this.account_balance = account_balance;
        this.isDeleted = isDeleted;
        this.expirationTime = expirationTime;
        this.autoRenewPeriod = autoRenewPeriod;
        this.deposits = deposits;
        this.loans = loans;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public long getAccount_balance() {
        return account_balance;
    }

    public long getSSP_Balance() {
        return SSP_Balance;
    }

    public void setSSP_Balance(long SSP_Balance) {
        this.SSP_Balance = SSP_Balance;
    }

    public void setAccount_balance(long account_balance) {
        this.account_balance = account_balance;
    }

    public boolean isAssociated() {
        return isAssociated;
    }

    public void setAssociated(boolean associated) {
        isAssociated = associated;
    }


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Duration getAutoRenewPeriod() {
        return autoRenewPeriod;
    }

    public void setAutoRenewPeriod(Duration autoRenewPeriod) {
        this.autoRenewPeriod = autoRenewPeriod;
    }

    public List<Deposit> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<Deposit> deposits) {
        this.deposits = deposits;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
}
