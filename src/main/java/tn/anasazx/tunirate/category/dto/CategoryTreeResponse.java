package tn.anasazx.tunirate.category.dto;

import java.util.List;

// A category with its subcategories and how many live (published) products each has
public record CategoryTreeResponse(
        Long id,
        String name,
        long productCount,
        List<Node> subcategories
) {
    public record Node(Long id, String name, long productCount) {}
}
