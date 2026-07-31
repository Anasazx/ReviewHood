package tn.anasazx.tunirate.dashboard.companyDashboard.service;

import tn.anasazx.tunirate.dashboard.companyDashboard.dto.CompanyDashboardResponse;

public interface CompanyDashboardService {

    CompanyDashboardResponse getMyCompanyDashboard(Long currentUserId);

}
