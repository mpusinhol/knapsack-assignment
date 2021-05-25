package com.mobiquity.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item implements Serializable {

    @EqualsAndHashCode.Include
    private Integer id;
    private Double weight;
    private Double value;
}
