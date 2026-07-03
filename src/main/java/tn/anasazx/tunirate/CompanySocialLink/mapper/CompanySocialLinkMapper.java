package tn.anasazx.tunirate.CompanySocialLink.mapper;

import tn.anasazx.tunirate.CompanySocialLink.dto.CompanySocialLinkRequest;
import tn.anasazx.tunirate.CompanySocialLink.dto.CompanySocialLinkResponse;
import tn.anasazx.tunirate.CompanySocialLink.entity.CompanySocialLink;

public class CompanySocialLinkMapper {

    public static CompanySocialLinkResponse toResponse(CompanySocialLink companySocials){
        return new CompanySocialLinkResponse(
                companySocials.getId(),
                companySocials.getCompany().getName(),
                companySocials.getPlatform(),
                companySocials.getUrl()
        );
    }

    public static CompanySocialLink toEntity(CompanySocialLinkRequest companySocialsRequest){

        CompanySocialLink companySocialLink = new CompanySocialLink();
        companySocialLink.setPlatform(companySocialsRequest.platform());
        companySocialLink.setUrl(companySocialsRequest.url());

        return companySocialLink;

    }

}
