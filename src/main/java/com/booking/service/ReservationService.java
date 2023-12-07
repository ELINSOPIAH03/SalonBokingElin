package com.booking.service;

import com.booking.models.Customer;
import com.booking.models.Employee;
import com.booking.models.Membership;
import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;
import com.booking.repositories.PersonRepository;
import com.booking.repositories.ServiceRepository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationService {
    @NonNull
    static PrintService printService = new PrintService();

    // Daftar dan objek Scanner
    static List<Person> personList = PersonRepository.getAllPerson();
    static List<Service> serviceList = ServiceRepository.getAllService();
    static List<Reservation> reservationList = new ArrayList<>();
    static Scanner input = new Scanner(System.in);
    
    public static void createReservation(){
        System.out.println("Membuat Reservasi");
    
        printService.showAllCustomer(personList);
    
        System.out.println("\nSilahkan Masukkan Customer Id:");
        String customerId = input.nextLine();
        Customer customer = findCustomerById(customerId);
    
        if (customer != null) {
            printService.showAllEmployee(personList);
    
            System.out.println("\nSilahkan Masukkan Employee Id:");
            String employeeId = input.nextLine();
            Employee employee = findEmployeeById(employeeId);
    
            if (employee != null) {
                List<Service> selectedServices = new ArrayList<>();
                boolean chooseAnotherService;
    
                do {
                    printService.showAllService(serviceList);
    
                    System.out.println("\nSilahkan Masukkan Service Id:");
                    String serviceId = input.nextLine();
                    Service service = findServiceById(serviceId);
    
                    if (service != null) {
                        if (!selectedServices.contains(service)) {
                            selectedServices.add(service);
                            System.out.println("Ingin pilih service yang lain (Y/T)?");
                            chooseAnotherService = input.nextLine().equalsIgnoreCase("Y");
                        } else {
                            System.out.println("Service sudah dipilih");
                            chooseAnotherService = true;
                        }
                    } else {
                        System.out.println("Service yang dicari tidak tersedia");
                        chooseAnotherService = true;
                    }
    
                } while (chooseAnotherService);
    
                double totalBookingPrice = calculateTotalBookingPrice(selectedServices, customer.getMember());
    
                Reservation reservation = new Reservation(
                        "Rsv-0" + (reservationList.size() + 1),
                        customer,
                        employee,
                        selectedServices,
                        totalBookingPrice,
                        "In Process"
                );
                

                TemporaryReservationStorage.addTemporaryReservation(reservation);
                reservationList.add(reservation);

                // Print log untuk melihat apakah reservasi berhasil ditambahkan
                System.out.println("Reservasi berhasil ditambahkan:");
                System.out.println("\n"+reservation);
    
                customer.reduceWallet(totalBookingPrice);
    
                // Print log untuk melihat uang pelanggan setelah reservasi
                System.out.println("\nUang pelanggan setelah reservasi: " + customer.getWallet());
    
                System.out.println("\nBooking Berhasil!");
                System.out.println("Total Biaya Booking : Rp. " + totalBookingPrice);
            } else {
                System.out.println("\nEmployee yang dicari tidak tersedia");
            }
        } else {
            System.out.println("\nCustomer yang dicari tidak tersedia");
        }
    }
    

    public static void getCustomerByCustomerId(){
        
    }

    public static void editReservationWorkstage(){
        
    }

    private static Customer findCustomerById(String customerId) {
        return personList.stream()
                .filter(person -> person instanceof Customer)
                .map(person -> (Customer) person)
                .filter(customer -> customer.getId().equalsIgnoreCase(customerId))
                .findFirst()
                .orElse(null);
    }

    private static Employee findEmployeeById(String employeeId) {
        return personList.stream()
                .filter(person -> person instanceof Employee)
                .map(person -> (Employee) person)
                .filter(employee -> employee.getId().equalsIgnoreCase(employeeId))
                .findFirst()
                .orElse(null);
    }

    private static Service findServiceById(String serviceId) {
        return serviceList.stream()
                .filter(service -> service.getServiceId().equalsIgnoreCase(serviceId))
                .findFirst()
                .orElse(null);
    }

    private static double calculateTotalBookingPrice(List<Service> services, Membership member) {
        double totalPrice = services.stream()
                .mapToDouble(Service::getPrice)
                .sum();

        if (member != null) {
            switch (member.getMembershipName()) {
                case "silver":
                    return totalPrice * 0.95; // Diskon 5%
                case "gold":
                    return totalPrice * 0.9; // Diskon 10%
                default:
                    return totalPrice;
            }
        }

        return totalPrice;
    }
    // Silahkan tambahkan function lain, dan ubah function diatas sesuai kebutuhan
}
