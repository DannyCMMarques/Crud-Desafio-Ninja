package com.crud.demo.utils;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder<T> {
    private Specification<T> spec = Specification.where(null);

    public SpecificationBuilder<T> add(Specification<T> newSpec) {
        if (newSpec != null) {
            spec = spec.and(newSpec);
        }
        return this;
    }

    public Specification<T> build() {
        return spec;
    }
}
