package tn.anasazx.tunirate.companySocialLink.dto;

import tn.anasazx.tunirate.enums.SocialPlatform;

public record CompanySocialLinkRequest(
        SocialPlatform platform,
        String url
) {}