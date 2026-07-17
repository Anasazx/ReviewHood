package tn.anasazx.tunirate.company.dto;

import tn.anasazx.tunirate.enums.CompanyStatus;

public record CompanyStatusRequest(
        CompanyStatus status
) {}