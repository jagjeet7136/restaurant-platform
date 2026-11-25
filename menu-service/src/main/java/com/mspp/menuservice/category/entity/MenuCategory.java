package com.mspp.menuservice.category.entity;

import com.mspp.menuservice.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "menu_categories",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_category_restaurant_name",
                        columnNames = {"restaurant_id", "name"}
                )
        }
)
public class MenuCategory extends BaseEntity {

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private boolean active = true;
}