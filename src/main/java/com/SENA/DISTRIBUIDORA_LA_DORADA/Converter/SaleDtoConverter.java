package com.SENA.DISTRIBUIDORA_LA_DORADA.Converter;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.SaleDetailDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.SaleResponseDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Client;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Sale;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.SaleDetail;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SaleDtoConverter {

    public SaleResponseDto toResponseDto(Sale sale) {
        SaleResponseDto response = new SaleResponseDto();
        response.setId(sale.getId());
        response.setStatus(sale.getStatus());

        // Cliente
        Client client = sale.getClient();
        response.setClientName(client != null ? client.getName() : "Sin cliente");
        response.setClientPhone(client != null ? client.getPhone() : "");
        response.setClientAddress(client != null ? client.getAddress() : "");

        // Fecha: java.util.Date → LocalDateTime
        if (sale.getDate() != null) {
            response.setDate(sale.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        } else {
            response.setDate(null);
        }

        // Total
        response.setTotal(sale.getTotal());

        // Detalles
        response.setDetails(toDetailDtos(sale.getDetails()));

        return response;
    }

    private List<SaleDetailDto> toDetailDtos(List<SaleDetail> details) {
        if (details == null) return List.of();

        return details.stream().map(detail -> {
            SaleDetailDto dto = new SaleDetailDto();
            dto.setProductName(detail.getProduct() != null ? detail.getProduct().getName() : "Producto eliminado");
            dto.setProductModel(detail.getProduct() != null ? detail.getProduct().getModel() : "Sin diseño");
            dto.setQuantity(detail.getQuantity());
            dto.setUnitPrice(detail.getUnitPrice());
            dto.setSubtotal(detail.getSubtotal());
            return dto;
        }).collect(Collectors.toList());
    }
}