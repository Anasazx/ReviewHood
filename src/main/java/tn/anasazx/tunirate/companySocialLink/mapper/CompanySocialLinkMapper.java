package tn.anasazx.tunirate.companySocialLink.mapper;

import tn.anasazx.tunirate.companySocialLink.dto.CompanySocialLinkRequest;
import tn.anasazx.tunirate.companySocialLink.dto.CompanySocialLinkResponse;
import tn.anasazx.tunirate.companySocialLink.entity.CompanySocialLink;

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
