package io.sytac.rabobank.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction implements Serializable {

    private int transactionReference;
    private String iban;
    private String description;
    private Double startBalance;
    private Double mutation;
    private Double endBalance;
    private boolean isNotValid = false;
    private List<Transaction> duplicatedTransactions = new ArrayList<>();
    private List<Irregularities> irregularities = new ArrayList<>();

    public List<Transaction> getDuplicatedTransactions() {
        synchronized (this){
            return duplicatedTransactions;
        }
    }

    public synchronized void addDuplicatedTransaction(Transaction transaction){
            duplicatedTransactions.add(transaction);
            isNotValid = true;
            if(!irregularities.contains(Irregularities.DUPLICATED_REF_NUMB)){
                addIrregularity(Irregularities.DUPLICATED_REF_NUMB);
            }
    }

    public synchronized boolean isDuplicated(){
        return !duplicatedTransactions.isEmpty();
    }

    public boolean isNotValid() {
        return isNotValid;
    }

    public void setNotValid(boolean notValid) {
        isNotValid = notValid;
    }

    public int getTransactionReference() {
        return transactionReference;
    }

    public String getDescription() {
        return description;
    }

    public List<Irregularities> getIrregularities() {
        return irregularities;
    }

    public void addIrregularity (Irregularities irregularity){
        irregularities.add(irregularity);
    }
}
