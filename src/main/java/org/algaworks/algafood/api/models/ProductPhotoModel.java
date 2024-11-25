package org.algaworks.algafood.api.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPhotoModel {

    private Long id;
    private String fileName;
    private String description;
    private String contentType;
    private Long size;
}
