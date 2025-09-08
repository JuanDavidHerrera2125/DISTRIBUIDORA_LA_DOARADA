package com.SENA.DISTRIBUIDORA_LA_DORADA.DTO;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.SaleDetailDto;

import java.util.List;

public class SaleDto {
    private Long id;
    private String clientName;
    private Double total;
    private List<SaleDetailDto> details;
}
