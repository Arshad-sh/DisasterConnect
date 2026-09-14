package com.disasterconnect.dto;

import com.disasterconnect.enums.ResourceCategory;

public class ResourceResponseDTO {

    private Long id;
    private String name;
    private String description;
    private ResourceCategory category;
    private Integer quantity;
    private String unit;
    private boolean available;
    private String location;
    private Long ngoId;

    public ResourceResponseDTO(
            Long id,
            String name,
            String description,
            ResourceCategory category,
            Integer quantity,
            String unit,
            boolean available,
            String location,
            Long ngoId) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.available = available;
        this.location = location;
        this.ngoId = ngoId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ResourceCategory getCategory() {
        return category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getLocation() {
        return location;
    }

    public Long getNgoId() {
        return ngoId;
    }
}