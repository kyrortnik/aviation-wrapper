package com.mpiatrenka.aviationwrapper.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AirportDetails {

    private String icaoId;
    private String name;
    private String state;
    private String country;
    private String latitude;
    private String longitude;
    private String type;
    private List<Runway> runways;

}
