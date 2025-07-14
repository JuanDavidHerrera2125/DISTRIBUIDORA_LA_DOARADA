package com.SENA.DISTRIBUIDORA_LA_DORADA.Entity;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.InstallmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.Date;

@Entity
@Table(name = "installments")
@Getter
@Setter
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

}
