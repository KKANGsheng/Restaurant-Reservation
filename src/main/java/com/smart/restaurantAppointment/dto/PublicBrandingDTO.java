package com.smart.restaurantAppointment.dto;

import com.smart.restaurantAppointment.entity.Merchant;
import lombok.Data;

@Data
public class PublicBrandingDTO {
    private String brandName;
    private String slug;
    private String brandColor;
    private String logo;

    public static PublicBrandingDTO from(Merchant merchant) {
        PublicBrandingDTO dto = new PublicBrandingDTO();
        dto.setSlug(merchant.getSlug());
        dto.setBrandName(merchant.getBusinessName());
        dto.setBrandColor(merchant.getBrandColor());
        return dto;
    }
}
