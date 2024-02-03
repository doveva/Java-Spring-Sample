package test.springtest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import test.springtest.models.WeatherForecast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class TestController {
    @GetMapping("/weatherForecast")
    List<WeatherForecast> AllForecast(){
        List<WeatherForecast> result = new ArrayList<>() {
        };
        for(int i=0; i < 5; i++){
            WeatherForecast AddValue = new WeatherForecast(
                    UUID.randomUUID(),
                    "",
                    (float) 17.2
            );
            result.add(AddValue);
        }
        return result;
    }
}
