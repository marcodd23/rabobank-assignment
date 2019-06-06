package io.sytac.rabobank.app.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReportItem implements Serializable {

    private int transactionReference;
    private String Description;
    private List<ValidationErrors> irregularities = new ArrayList<>();

    public void addCause(ValidationErrors irregularity){
        irregularities.add(irregularity);
    }
}
