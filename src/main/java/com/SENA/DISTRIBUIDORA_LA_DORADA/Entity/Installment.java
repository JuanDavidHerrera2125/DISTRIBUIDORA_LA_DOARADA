package com.SENA.DISTRIBUIDORA_LA_DORADA.Entity;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.InstallmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.Date;

@Entity
@Table(name = "installments")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Column (name = "installment_number")
    private Integer installmentNumber;

    @Temporal(TemporalType.DATE)
    @Column (name = "scheduled_date")
    private Date scheduledDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "installment_status")
    private InstallmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id" , nullable = false)
    private Sale sale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public InstallmentStatus getStatus() {
        return status;
    }

    public void setStatus(InstallmentStatus status) {
        this.status = status;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }
}
