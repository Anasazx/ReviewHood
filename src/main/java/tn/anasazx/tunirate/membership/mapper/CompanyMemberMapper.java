package tn.anasazx.tunirate.membership.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.membership.dto.CompanyMemberResponse;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.user.mapper.UserMapper;

@RequiredArgsConstructor
@Component
public class CompanyMemberMapper {

    public CompanyMemberResponse toResponse(CompanyMember m) {
        return new CompanyMemberResponse(
                UserMapper.toResponse(m.getUser()),
                m.getCompany().getId(),
                m.getCompany().getName(),
                m.getCompanyRole(),
                m.getJoinedAt()
        );
    }

}
