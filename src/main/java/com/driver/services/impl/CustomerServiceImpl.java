package com.driver.services.impl;

import com.driver.model.TripBooking;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;
import com.driver.model.TripStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer = customerRepository2.findById(customerId).get();
		customerRepository2.delete(customer);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		List<Driver> driverList = driverRepository2.findAll();

		Driver driver = null;
		int id = Integer.MAX_VALUE;
		for (Driver driver1 : driverList){
			if(driver1.getDriverId() < id && driver1.getCab().getAvailable()==true){
				id = driver1.getDriverId();
				driver = driver1;
			}
		}

		if(driver==null){
			throw new Exception("No cab available!");
		}

		TripBooking tripBooking = new TripBooking();

		Customer customer = customerRepository2.findById(customerId).get();

		tripBooking.setCustomer(customer);
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setDistanceInKm(distanceInKm);
		tripBooking.setStatus(TripStatus.CONFIRMED);
		tripBooking.setBill(0);
		tripBooking.setDriver(driver);

//		List<TripBooking> tripBookingList = driver.getTripBookingList();
//		tripBookingList.add(tripBooking);
//		driver.setTripBookingList(tripBookingList);
		driver.getCab().setAvailable(false);

		driverRepository2.save(driver);
		// tripBookingRepository2.save(tripBooking);
		customerRepository2.save(customer);

		return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();


		Driver driver = tripBooking.getDriver();

		driver.getCab().setAvailable(true);

		List<TripBooking> tripBookingList = driver.getTripBookingList();
		tripBookingList.remove(tripBooking);

//		int bill = tripBooking.getDistanceInKm() * 10;
		tripBooking.setBill(0);
		tripBooking.setStatus(TripStatus.CANCELED);
		tripBookingList.add(tripBooking);

		//driverRepository2.save(driver);
		tripBookingRepository2.save(tripBooking);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();

		Driver driver = tripBooking.getDriver();
		driver.getCab().setAvailable(true);

		List<TripBooking> tripBookingList = driver.getTripBookingList();
		tripBookingList.remove(tripBooking);

		int bill = 10 * tripBooking.getDistanceInKm();
		tripBooking.setBill(bill);
		tripBooking.setStatus(TripStatus.COMPLETED);
		//driverRepository2.save(driver);
		tripBookingRepository2.save(tripBooking);
	}
}
