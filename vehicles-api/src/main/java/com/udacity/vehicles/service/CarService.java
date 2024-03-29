package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository repository;
    private final MapsClient mapsClient;
    private final PriceClient priceClient;

    public CarService(CarRepository repository,  MapsClient mapsClient, PriceClient priceClient) {
        /**
         *  Add the Maps and Pricing Web Clients you create
         *   in `VehiclesApiApplication` as arguments and set them here.
         */
        this.repository = repository;
        this.mapsClient = mapsClient;
        this.priceClient = priceClient;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {

        List<Car> allCars = repository.findAll();

        for (Car currentCar: allCars
        ) {
            currentCar.setPrice(priceClient.getPrice(currentCar.getId()));
            currentCar.setLocation(mapsClient.getAddress(currentCar.getLocation()));
        }


        return allCars;
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        /**
         *  Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         *   Remove the below code as part of your implementation.
         */

        Optional<Car> optionalCar = repository.findById(id);

        if (!optionalCar.isPresent()){throw  new CarNotFoundException("Could not find Car with provided id!");}



        Car car = optionalCar.get();

        /**
         * Use the Pricing Web client you create in `VehiclesApiApplication`
         *   to get the price based on the `id` input'
         *  Set the price of the car
         * Note: The car class file uses @transient, meaning you will need to call
         *   the pricing service each time to get the price.
         */


        car.setPrice(priceClient.getPrice(car.getId()));


        /**
         *  Use the Maps Web client you create in `VehiclesApiApplication`
         *   to get the address for the vehicle. You should access the location
         *   from the car object and feed it to the Maps service.
         *  Set the location of the vehicle, including the address information
         * Note: The Location class file also uses @transient for the address,
         * meaning the Maps service needs to be called each time for the address.
         */


        car.setLocation(mapsClient.getAddress(car.getLocation()));

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());

                        // Added Condition Update
                        carToBeUpdated.setCondition(car.getCondition());

                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }
        //Obtain ID for Car, save
        Car newCar = repository.save(car);

        //want to show Price and Location after Saving Car so Price and Maps Service will be used
        newCar.setLocation(this.mapsClient.getAddress(car.getLocation()));
        newCar.setPrice(this.priceClient.getPrice(newCar.getId()));

        return repository.save(newCar);
    }

    public Car saveWithNoLocationGen(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        //not needed, get no new location
                        /*carToBeUpdated.setLocation(car.getLocation());*/
                        // Added Condition Update
                        carToBeUpdated.setCondition(car.getCondition());
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }
        //Obtain ID for Car, save
        Car newCar = repository.save(car);

        //want to show Pricae And Location after Saving Car so Price and Maps Service will be used
        car.setLocation(this.mapsClient.getAddress(car.getLocation()));
        car.setPrice(this.priceClient.getPrice(newCar.getId()));

        return repository.save(car);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {

        Optional<Car> car = repository.findById(id);

        if (!car.isPresent()){throw new CarNotFoundException("Could not find Car with provided id");}

        else {repository.deleteById(id);}

        /**
         *  Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         */


        /**
         *  Delete the car from the repository.
         */


    }
}
