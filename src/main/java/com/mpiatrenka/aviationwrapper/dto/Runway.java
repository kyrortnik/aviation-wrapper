package com.mpiatrenka.aviationwrapper.dto;

import lombok.Data;

@Data
public class Runway {

    private String id;
    private String dimension;
    private Character surface;
    private Integer alignment;
}
