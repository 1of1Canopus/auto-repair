package com.housedevinci.autorepair.quote.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.housedevinci.autorepair.quote.domain.Part;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateMechanicalPartRequest.class, name = "MECHANICAL"),
        @JsonSubTypes.Type(value = CreateFluidPartRequest.class, name = "FLUID")
})
public sealed interface CreatePartRequest permits CreateMechanicalPartRequest, CreateFluidPartRequest {

    Part toDomain();
}
