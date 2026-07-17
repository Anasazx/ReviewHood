package tn.anasazx.tunirate.product.dto;

import tn.anasazx.tunirate.enums.ProductStatus;

public record ProductStatusRequest(
   ProductStatus status
) {}