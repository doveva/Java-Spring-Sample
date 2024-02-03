package test.springtest.models;

import java.util.UUID;

public class WeatherForecast
{
    public UUID id;
    public String name;
    public Float value;
    public WeatherForecast(
            UUID id,
            String name,
            Float value
    ){
        this.id = id;
        this.name = name;
        this.value = value;
    }
}
