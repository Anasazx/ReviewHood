package tn.anasazx.tunirate.dashboard.adminDashboard.dto;

import java.time.LocalDate;

public record TimeSeriesPointDTO(LocalDate date, long count) {}
