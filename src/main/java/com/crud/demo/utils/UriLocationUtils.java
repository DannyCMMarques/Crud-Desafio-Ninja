package com.crud.demo.utils;

import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class UriLocationUtils {

    public static URI criarLocationUri(String path, Object... uriVariables) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(path)
                .buildAndExpand(uriVariables)
                .toUri();
    }
}
