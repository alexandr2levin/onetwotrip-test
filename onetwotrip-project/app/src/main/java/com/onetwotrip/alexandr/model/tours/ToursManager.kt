package com.onetwotrip.alexandr.model.tours

import com.onetwotrip.alexandr.data.companies.CompaniesDao
import com.onetwotrip.alexandr.data.companies.Company
import com.onetwotrip.alexandr.data.flights.Flight
import com.onetwotrip.alexandr.data.flights.FlightsDao
import com.onetwotrip.alexandr.data.hotels.Hotel
import com.onetwotrip.alexandr.data.hotels.HotelsDao
import io.reactivex.Single
import io.reactivex.functions.Function3

class ToursManager(
    private val companiesDao: CompaniesDao,
    private val hotelsDao: HotelsDao,
    private val flightsDao: FlightsDao
) {

    fun getCompanies() = companiesDao.getAll()

    fun search(params: SearchParams): Single<List<Tour>> {
        return Single.zip(
                companiesDao.getAll(),
                hotelsDao.getAll(),
                flightsDao.getAll(),
                Function3<List<Company>,  List<Hotel>, List<Flight>, List<Tour>> { companies, hotels, flights ->
                    val hotelsToTour = if(params.companiesIds.isEmpty()) {
                        // there is no filters
                        hotels
                    } else {
                        // filter by companies
                        hotels
                            .filter {
                                it.flights.any { flightId -> params.companiesIds.contains(flightId) }
                            }
                    }

                    hotelsToTour
                        .map { hotel ->
                            createTour(companies, flights, hotel)
                        }
                }
            )
    }

    private fun createTour(companies: List<Company>, flights: List<Flight>, hotel: Hotel): Tour {
        return Tour(
            hotelId = hotel.id,
            hotelName = hotel.name,
            hotelPrice = hotel.price,
            flightOptions = createFlightOptions(companies, flights, hotel)
        )
    }

    private fun createFlightOptions(companies: List<Company>, flights: List<Flight>, hotel: Hotel): List<Tour.FlightOption> {
        val hotelFlightsIds = hotel.flights

        val hotelFlights = hotelFlightsIds
            .map { hotelFlightId ->
                flights.first { it.id == hotelFlightId }
            }

        return hotelFlights
            .map { flight ->
                val company = companies.first { company -> flight.companyId == company.id }
                Tour.FlightOption(
                    flightId = flight.id,
                    companyName = company.name,
                    departive = flight.departure,
                    arrival = flight.arrival,
                    price = flight.price
                )
            }
    }

}