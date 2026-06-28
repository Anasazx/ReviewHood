package tn.anasazx.tunirate.enums;

public enum ProductStatus {
    DRAFT,             // Company is still editing
    PENDING_REVIEW,    // Waiting for admin approval
    PUBLISHED,         // Visible to users
    REJECTED,          // Admin rejected it
    ARCHIVED           // No longer sold, but history is preserved
}