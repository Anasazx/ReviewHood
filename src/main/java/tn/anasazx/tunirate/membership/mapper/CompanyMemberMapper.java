package tn.anasazx.tunirate.membership.mapper;

import tn.anasazx.tunirate.company.mapper.CompanyMapper;
import tn.anasazx.tunirate.membership.dto.CompanyMemberResponse;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.user.mapper.UserMapper;

public class CompanyMemberMapper {

    public static CompanyMemberResponse toResponse(CompanyMember m) {
        return new CompanyMemberResponse(
                UserMapper.toResponseDto(m.getUser()),
                CompanyMapper.toResponse(m.getCompany()),
                m.getRole(),
                m.getJoinedAt()
        );
    }

}
