package tn.anasazx.tunirate.dashboard;

import tn.anasazx.tunirate.dashboard.adminDashboard.dto.TimeSeriesPointDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Turns [day, count] rows from native queries into one point per day, zeros included
public final class TimeSeries {

    private TimeSeries() {}

    public static List<TimeSeriesPointDTO> daily(List<Object[]> rows, LocalDate startDate, int days) {
        Map<LocalDate, Long> countsByDate = rows.stream()
                .collect(Collectors.toMap(row -> toLocalDate(row[0]), row -> ((Number) row[1]).longValue()));

        List<TimeSeriesPointDTO> filled = new ArrayList<>();
        for (int i = 0; i <= days; i++) {
            LocalDate date = startDate.plusDays(i);
            filled.add(new TimeSeriesPointDTO(date, countsByDate.getOrDefault(date, 0L)));
        }
        return filled;
    }

    private static LocalDate toLocalDate(Object value) {
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        if (value instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate();
        }
        if (value instanceof java.sql.Timestamp timestamp) {
            return timestamp.toLocalDateTime().toLocalDate();
        }
        throw new IllegalStateException("Unexpected date type: " + value.getClass());
    }
}
