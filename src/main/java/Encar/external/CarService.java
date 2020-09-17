
package Encar.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//@FeignClient(name="car", url="http://car:8080")
@FeignClient(name="Car", url="${api.url.car}")
public interface CarService {

    @RequestMapping(method= RequestMethod.GET, path="/cars/check",produces = "application/json")
    //public @ResponseBody String checkCarQuantity(@RequestBody Car car);
    @ResponseBody String checkCarQuantity(@RequestParam("carId") String carId);

}