package com.mobiquity.implementation.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item implements Serializable {

    @EqualsAndHashCode.Include
    private Integer id;
    private Double weight;
    private Double value;
}
