package tn.anasazx.tunirate.CompanySocialLink.dto;

import tn.anasazx.tunirate.enums.SocialPlatform;

public record CompanySocialLinkRequest(
        SocialPlatform platform,
        String url
) {}