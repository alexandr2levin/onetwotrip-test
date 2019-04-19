package com.onetwotrip.alexandr.model.tours

import com.onetwotrip.alexandr.data.companies.CompaniesDao
import com.onetwotrip.alexandr.data.companies.Company
import com.onetwotrip.alexandr.data.flights.Flight
import com.onetwotrip.alexandr.data.flights.FlightsDao
import com.onetwotrip.alexandr.data.hotels.Hotel
import com.onetwotrip.alexandr.data.hotels.HotelsDao
import io.reactivex.Single
import io.reactivex.functions.Function3
import org.threeten.bp.Duration

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
                    filterHotels(params, hotels, flights)
                        .map { hotel ->
                            createTour(companies, flights, hotel)
                        }
                }
            )
    }

    private fun filterHotels(params: SearchParams, hotels: List<Hotel>, flights: List<Flight>): List<Hotel> {
        if(params.companiesIds.isEmpty()) {
            return hotels
        }
        return hotels
                .map {
                    val filteredFlights = it.flights
                            .filter { flightId ->
                                val flight = flights.first { it.id == flightId }
                                params.companiesIds.contains(flight.companyId)
                            }

                    it.copy(flights = filteredFlights)
                }
                .filter { it.flights.isNotEmpty() }
    }

    private fun createTour(companies: List<Company>, flights: List<Flight>, hotel: Hotel): Tour {
        return Tour(
            hotelId = hotel.id,
            hotelName = hotel.name,
            hotelPrice = hotel.price,
            flightOptions = createFlightOptions(companies, flights, hotel)
        )
    }

    private fun createFlightOptions(
            companies: List<Company>, flights: List<Flight>, hotel: Hotel
    ): List<Tour.FlightOption> {
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
                    departure = flight.departure,
                    arrival = flight.arrival,
                    duration = Duration.between(flight.departure, flight.arrival),
                    priceWithHotel = flight.price + hotel.price
                )
            }
                .sortedBy { it.priceWithHotel }
    }

}