package com.SENA.DISTRIBUIDORA_LA_DORADA.DTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleResponseDto {
    private Long id;
    private String clientName;
    private String clientPhone;
    private String clientAddress;
    private LocalDateTime date;
    private Double total;
    private List<SaleDetailDto> details;
    private String status;
}