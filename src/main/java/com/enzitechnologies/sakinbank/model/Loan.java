package com.enzitechnologies.sakinbank.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String loan_id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "accountId"))
    private Account account;

    private long ssps_needed;

    private double principal;

    private long amount_repaid;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date_of_issue;

    private double repayment_percentage;

    public Loan(Account account, long ssps_needed, double principal, long amount_repaid, Date date_of_issue, double repayment_percentage) {
        this.account = account;
        this.ssps_needed = ssps_needed;
        this.principal = principal;
        this.amount_repaid = amount_repaid;
        this.date_of_issue = date_of_issue;
        this.repayment_percentage = repayment_percentage;
    }

    public String getLoan_id() {
        return loan_id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public long getSsps_needed() {
        return ssps_needed;
    }

    public void setSsps_needed(long ssps_needed) {
        this.ssps_needed = ssps_needed;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public Date getDate_of_issue() {
        return date_of_issue;
    }

    public void setDate_of_issue(Date date_of_issue) {
        this.date_of_issue = date_of_issue;
    }

    public double getRepayment_percentage() {
        return repayment_percentage;
    }

    public void setRepayment_percentage(double repayment_percentage) {
        this.repayment_percentage = repayment_percentage;
    }

    public long getAmount_repaid() {
        return amount_repaid;
    }

    public void setAmount_repaid(long amount_repaid) {
        this.amount_repaid = amount_repaid;
    }
}
