package tn.anasazx.tunirate.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(@NotBlank String name) {}