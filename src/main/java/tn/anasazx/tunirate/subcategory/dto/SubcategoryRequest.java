package tn.anasazx.tunirate.subcategory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubcategoryRequest(@NotBlank String name, @NotNull Long categoryId) {}