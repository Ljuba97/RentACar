package com.rentACar.controller;

import com.rentACar.dao.*;
import com.rentACar.model.CarModel;
import com.rentACar.model.request.CarAddRequestModel;
import com.rentACar.model.request.CarUpdateRequestModel;
import com.rentACar.model.response.CarAddResponseModel;
import com.rentACar.model.response.CarDeleteResponseModel;
import com.rentACar.model.response.CarResponseModel;
import com.rentACar.model.response.CarUpdateResponseModel;
import org.apache.tomcat.jni.Local;
import org.springframework.web.bind.annotation.*;
import static java.time.temporal.ChronoUnit.DAYS;


import java.time.LocalDate;
import java.util.*;

@RestController
public class CarController {
    private static final CarDao cd = new CarDaoSQL();
    private static final UserDao ud = new UserDaoSQL();
    private static final ContractDao cod = new ContractDaoSQL();

    @GetMapping("/cars")
    public List<CarResponseModel> allCars() {
        return cd.allCars();
    }

    @GetMapping("/cars/search")
    public List<CarResponseModel> searchCars(@RequestParam(value = "make") String make, @RequestParam(value = "model") String model, @RequestParam(value = "year") int year, @RequestParam(value = "automatic") boolean automatic, @RequestParam(value = "price") double price, @RequestParam(value = "power") int power, @RequestParam(value = "doors") int doors) {
        return cd.searchCars(make, model, year, automatic, price, power, doors);
    }

    @GetMapping("/cars/{carId}")
    public CarResponseModel getCarById(@PathVariable("carId") UUID carId) {
        return cd.getCarById(carId);
    }

    @PatchMapping("/cars/{carId}")
    public CarUpdateResponseModel updateCar(@RequestHeader(value = "authorization") UUID adminId, @PathVariable("carId") UUID carId, @RequestBody CarUpdateRequestModel car) {
        for (var x : ud.getAllAdminId()) {
            if (x.equals(adminId)) {
                cd.updateCar(car, carId);
                return new CarUpdateResponseModel(true, "Changed ...");
            }
        }
        return new CarUpdateResponseModel(false, "Authorization problem!");
    }

    @DeleteMapping("/cars/{carId}")
    public CarDeleteResponseModel deleteCar(@PathVariable("carId") UUID carId, @RequestHeader(value = "authorization") UUID adminId) {
        boolean admin = false;
        for (var x : ud.getAllAdminId()) {
            if (x.equals(adminId)) {
                admin = true;
                break;
            }
        }

        if (admin) {
            for (var x : cd.allCars()) {
                if (x.getCarId().equals(carId)) {
                    cd.deleteCar(carId);
                    return new CarDeleteResponseModel(true, "Car successfully removed!");
                }
            }
            return new CarDeleteResponseModel(false, "Car with this id doesn't exist!");
        }
        return new CarDeleteResponseModel(false, "Authorization problem!");
    }

    @PostMapping("/cars")
    public CarAddResponseModel addCar(@RequestHeader(value = "authorization") UUID adminId, @RequestBody CarAddRequestModel car) {
        for (var x : ud.getAllAdminId()) {
            if (x.equals(adminId)) {
                cd.addCar(car);
                return new CarAddResponseModel(true, "Car successfully added!");
            }
        }
        return new CarAddResponseModel(false, "Authorization problem!");
    }

    @GetMapping("/cars/available")
    public List<CarResponseModel> allAvailableCars(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate) {
        List<CarResponseModel> list = new ArrayList<>();
        List<LocalDate> searchDates = cod.betweenDates(LocalDate.parse(startDate), LocalDate.parse(endDate));
        HashSet<UUID> unavailableCarId = new HashSet<>();

        for (Map.Entry<String, List<LocalDate>> e : cod.allBetweenDates().entrySet()) {
            for (var x : searchDates) {
                if (e.getValue().contains(x)) {
                    unavailableCarId.add(UUID.fromString(e.getKey()));
                    break;
                }
            }
        }

        for (var x : cd.allCars()) {
            if (!unavailableCarId.contains(x.getCarId()))
                list.add(x);
        }

        return list;
    }

    @GetMapping("cars/available/search")
    public List<CarResponseModel> searchAvailableCars(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, @RequestParam(value = "year", required = false) int year, @RequestParam(value = "make", required = false) String make, @RequestParam(value = "model", required = false) String model, @RequestParam(value = "automatic", required = false) boolean automatic, @RequestParam(value = "price", required = false) double price, @RequestParam(value = "power", required = false) int power, @RequestParam(value = "doors", required = false) int doors) {
        List<CarResponseModel> list = new ArrayList<>();
        List<LocalDate> searchDates = cod.betweenDates(LocalDate.parse(startDate), LocalDate.parse(endDate));
        HashSet<UUID> unavailableCarId = new HashSet<>();

        for (Map.Entry<String, List<LocalDate>> e : cod.allBetweenDates().entrySet()) {
            for (var x : searchDates) {
                if (e.getValue().contains(x)) {
                    unavailableCarId.add(UUID.fromString(e.getKey()));
                    break;
                }
            }
        }

        return cd.searchAvailableCars(LocalDate.parse(startDate), LocalDate.parse(endDate), make, model, year, automatic, price, power, doors, unavailableCarId);
    }

    @GetMapping("cars/{carId}/calendar")
    public List<LocalDate> calendar(@PathVariable("carId") UUID carId) {
        return cod.calendar(carId);
    }
}
