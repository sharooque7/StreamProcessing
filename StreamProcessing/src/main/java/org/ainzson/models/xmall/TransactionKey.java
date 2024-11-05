package org.ainzson.models.xmall;

import java.util.Date;
import java.util.Objects;

public class TransactionKey {

    private final String customerId;
    private final Date transactionDate;

    public TransactionKey(String customerId, Date transactionDate){
        this.transactionDate = transactionDate;
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionKey that)) return false;
        return Objects.equals(getCustomerId(), that.getCustomerId()) && Objects.equals(getTransactionDate(), that.getTransactionDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerId(), getTransactionDate());
    }
}
