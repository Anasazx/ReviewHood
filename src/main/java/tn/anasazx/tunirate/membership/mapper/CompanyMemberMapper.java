package tn.anasazx.tunirate.membership.mapper;

import tn.anasazx.tunirate.company.mapper.CompanyMapper;
import tn.anasazx.tunirate.membership.dto.CompanyMemberResponse;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.user.mapper.UserMapper;

public class CompanyMemberMapper {

    public static CompanyMemberResponse toResponse(CompanyMember m) {
        return new CompanyMemberResponse(
                UserMapper.toResponse(m.getUser()),
                CompanyMapper.toResponse(m.getCompany()),
                m.getCompanyRole(),
                m.getJoinedAt()
        );
    }

}
