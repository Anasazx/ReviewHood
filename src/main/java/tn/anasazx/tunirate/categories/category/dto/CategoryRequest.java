package tn.anasazx.tunirate.categories.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(@NotBlank String name) {}