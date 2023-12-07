package com.booking.service;

import java.util.ArrayList;
import java.util.List;

import com.booking.models.Reservation;

public class TemporaryReservationStorage {
    private static List<Reservation> temporaryReservations = new ArrayList<>();

    public static void addTemporaryReservation(Reservation reservation) {
        temporaryReservations.add(reservation);
    }

    public static List<Reservation> getTemporaryReservations() {
        return new ArrayList<>(temporaryReservations);
    }

    public static void clearTemporaryReservations() {
        temporaryReservations.clear();
    }
}
