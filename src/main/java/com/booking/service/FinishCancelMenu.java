package com.booking.service;

import java.util.List;
import java.util.Scanner;

import com.booking.models.Reservation;
import com.booking.models.Service;

public class FinishCancelMenu {
    private List<Reservation> reservationList;
    private Scanner input;

    public FinishCancelMenu(List<Reservation> reservationList, Scanner input) {
        this.reservationList = reservationList;
        this.input = input;
    }

    public void displayFinishCancelMenu() {
        System.out.println("Finish/Cancel Reservasi");
        System.out.println("Data di TemporaryReservationStorage: " + TemporaryReservationStorage.getTemporaryReservations());

        showInProcessReservations();

        System.out.print("Silahkan Masukkan Reservation Id: ");
        String reservationId = input.nextLine();

        // Mencari reservasi berdasarkan ID
        System.out.println("Isi reservationList sebelum: " + reservationList);
        Reservation reservation = findReservationById("Rsv-01");
        // Reservation foundReservation = findReservationById("Rsv-01");
        System.out.println("Isi reservationList setelah: " + reservationList);


        // Melakukan validasi reservasi
        if (reservation != null) {
            if (reservation.getWorkstage().equalsIgnoreCase("In Process")) {
                System.out.println("\nSelesaikan reservasi:");
                System.out.println("1. Finish");
                System.out.println("2. Cancel");

                System.out.print("Pilih aksi (1/2): ");
                int actionChoice = Integer.parseInt(input.nextLine());

                switch (actionChoice) {
                    case 1:
                        finishReservation(reservation);
                        System.out.println("\nReservasi dengan id " + reservationId + " sudah Finish\n");
                        break;
                    case 2:
                        cancelReservation(reservation);
                        System.out.println("\nReservasi dengan id " + reservationId + " sudah Cancel\n");
                        break;
                    default:
                        System.out.println("\nAksi tidak valid\n");
                        break;
                }
            } else {
                System.out.println("\nReservasi dengan id " + reservationId + " sudah selesai\n");
            }
        } else {
            System.out.println("\nReservation yang dicari tidak tersedia\n");
        }
    }

    private void showInProcessReservations() {
        System.out.printf("| %-3s | %-7s | %-15s | %-25s | %-11s |\n",
                "No.", "ID", "Nama Customer", "Nama Service", "Total Biaya");
        System.out.println("=============================================================================");
        TemporaryReservationStorage.getTemporaryReservations().stream()
            .filter(reservation -> reservation.getWorkstage().equalsIgnoreCase("In Process"))
            .forEach(this::displayReservation);
    }

    private void displayReservation(Reservation reservation) {
        System.out.printf("| %-3d | %-7s | %-15s | %-25s | %-11.2f |\n",
                reservationList.indexOf(reservation) + 1+1,
                reservation.getReservationId(),
                reservation.getCustomer().getName(),
                printServices(reservation.getServices()),
                reservation.getReservationPrice());
        
        // System.out.println("Reservation ID: " + reservation.getReservationId());
        // System.out.println("Customer Name: " + reservation.getCustomer().getName());
        // System.out.println("Services: " + printServices(reservation.getServices()));
        // System.out.println("Total Price: " + reservation.getReservationPrice());
        // System.out.println("Workstage: " + reservation.getWorkstage());
    }

    // private Reservation findReservationById(String reservationId) {
    //     return reservationList.stream()
    //             .filter(reservation -> reservation.getReservationId().equalsIgnoreCase(reservationId))
    //             .findFirst()
    //             .orElse(null);
    // }
    private Reservation findReservationById(String reservationId) {
        for (Reservation reservation : reservationList) {
            System.out.println("ID yang dicari: " + reservationId);
            System.out.println("ID dalam list: " + reservation.getReservationId());
            System.out.println("Workstage dalam list: " + reservation.getWorkstage());

            if (reservation.getReservationId().equalsIgnoreCase(reservationId)) {
                System.out.println("Reservasi ditemukan!");
                return reservation;
            }
        }
        System.out.println("Reservasi tidak ditemukan.");
        return null;
    }

    private void finishReservation(Reservation reservation) {
        reservation.setWorkstage("Finish");
        TemporaryReservationStorage.addTemporaryReservation(reservation);
    }

    private void cancelReservation(Reservation reservation) {
        reservationList.remove(reservation);
        TemporaryReservationStorage.addTemporaryReservation(reservation);
    }

    private String printServices(List<Service> services) {
        StringBuilder serviceNames = new StringBuilder();
        for (Service service : services) {
            serviceNames.append(service.getServiceName()).append(", ");
        }
        return serviceNames.toString().replaceAll(", $", ""); // Menghilangkan koma di akhir
    }
}
