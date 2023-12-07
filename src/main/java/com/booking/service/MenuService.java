package com.booking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.repositories.PersonRepository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;


@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuService {
    @NonNull
    static PrintService printService = new PrintService(); 
    
    private static List<Person> personList = PersonRepository.getAllPerson();
    private static List<Reservation> reservationList = new ArrayList<>();
    private static Scanner input = new Scanner(System.in);


    public static void mainMenu() {
        // reservationList = ReservationRepository.getAllReservation();
        String[] mainMenuArr = {"Show Data", "Create Reservation", "Complete/cancel reservation", "Exit\n"};
        String[] subMenuArr = {"Recent Reservation", "Show Customer", "Show Available Employee","Show Reservation History + Total Profit","Back to main menu\n"};
    
        int optionMainMenu;
        int optionSubMenu;

		boolean backToMainMenu = false;
        boolean backToSubMenu = false;
        PrintService printService = new PrintService();
        FinishCancelMenu finishCancelMenu = new FinishCancelMenu(reservationList, input);

        do {
            PrintService.printMenu("\nMain Menu", mainMenuArr);
            System.out.println("Choose :");
            optionMainMenu = Integer.valueOf(input.nextLine());
            switch (optionMainMenu) {
                case 1:
                    do {
                        PrintService.printMenu("\nShow Data", subMenuArr);
                        System.out.println("Choose :");
                        optionSubMenu = Integer.valueOf(input.nextLine());
                        // Sub menu - menu 1
                        switch (optionSubMenu) {
                            case 1:
                                // panggil fitur tampilkan recent reservation
                                // System.out.println("Ukuran Daftar Reservasi: " + reservationList.size());
                                // System.out.println("Isi Daftar Reservasi: " + reservationList);

                                // printService.showRecentReservation(reservationList);
                                printService.showRecentReservation();
                                break;
                            case 2:
                                // panggil fitur tampilkan semua customer
                                printService.showAllCustomer(personList);
                                break;
                            case 3:
                                // panggil fitur tampilkan semua employee
                                printService.showAllEmployee(personList);
                                break;
                            case 4:
                                // panggil fitur tampilkan history reservation + total keuntungan
                                printService.showHistoryReservation(reservationList);
                                break;
                            case 0:
                                backToSubMenu = true;
                        }
                    } while (!backToSubMenu);
                    break;
                case 2:
                    // panggil fitur menambahkan reservation
                    ReservationService.createReservation();
                    break;
                case 3:
                    // panggil fitur mengubah workstage menjadi finish/cancel
                    // ReservationService.finishOrCancelReservation();
                    finishCancelMenu.displayFinishCancelMenu();
                    break;
                case 0:
                    backToMainMenu = false;
                    break;
            }
        } while (!backToMainMenu);
		
	}

}
