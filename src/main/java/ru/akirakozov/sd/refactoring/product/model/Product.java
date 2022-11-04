package ru.akirakozov.sd.refactoring.product.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Product {
    @NonNull
    private String name;
    @NonNull
    private Long price;
}
