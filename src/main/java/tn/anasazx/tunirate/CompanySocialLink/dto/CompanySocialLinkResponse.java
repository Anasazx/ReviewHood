package tn.anasazx.tunirate.CompanySocialLink.dto;

import tn.anasazx.tunirate.enums.SocialPlatform;

public record CompanySocialLinkResponse(
        Long id,
        String companyId,
        SocialPlatform platform,
        String url
)
{}