package com.enzitechnologies.sakinbank.model;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Key;
import com.hedera.hashgraph.sdk.TokenId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    private String tokenId;
    private String name;
    private String symbol;
    private int decimals;
    private long totalSupply;
    private String treasury_acc;
    private String admin_key;
    private String supply_key;
    private String freeze_key;
    private String wipe_key;

    public Token() {
    }

    public Token(String tokenId,
                 String name,
                 String symbol,
                 int decimals,
                 long totalSupply,
                 String treasury_acc,
                 String admin_key,
                 String supply_key,
                 String freeze_key,
                 String wipe_key) {
        this.tokenId = tokenId;
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
        this.totalSupply = totalSupply;
        this.treasury_acc = treasury_acc;
        this.admin_key = admin_key;
        this.supply_key = supply_key;
        this.freeze_key = freeze_key;
        this.wipe_key = wipe_key;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public long getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(long totalSupply) {
        this.totalSupply = totalSupply;
    }

    public String getTreasury_acc() {
        return treasury_acc;
    }

    public void setTreasury_acc(String treasury_acc) {
        this.treasury_acc = treasury_acc;
    }

    public String getAdmin_key() {
        return admin_key;
    }

    public void setAdmin_key(String admin_key) {
        this.admin_key = admin_key;
    }

    public String getSupply_key() {
        return supply_key;
    }

    public void setSupply_key(String supply_key) {
        this.supply_key = supply_key;
    }

    public String getFreeze_key() {
        return freeze_key;
    }

    public void setFreeze_key(String freeze_key) {
        this.freeze_key = freeze_key;
    }

    public String getWipe_key() {
        return wipe_key;
    }

    public void setWipe_key(String wipe_key) {
        this.wipe_key = wipe_key;
    }
}
