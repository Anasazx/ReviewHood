package tn.anasazx.tunirate.productSuggestion.dto;

import tn.anasazx.tunirate.enums.SuggestionStatus;

public record UpdateStatusRequest(
   SuggestionStatus status,
   Long productId
) {}
