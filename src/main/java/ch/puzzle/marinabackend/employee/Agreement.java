package ch.puzzle.marinabackend.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ch.puzzle.marinabackend.AbstractEntity;

@Entity
@Table(name = "agreement")
public class Agreement extends AbstractEntity{
    @OneToOne(mappedBy = "agreement")
    @JsonBackReference
    private Employee employee;
    @Column(name = "agreement_pdf_path")
    private String agreementPdfPath;
    
    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public String getAgreementPdfPath() {
        return agreementPdfPath;
    }
    public void setAgreementPdfPath(String agreementPdfPath) {
        this.agreementPdfPath = agreementPdfPath;
    }
}
