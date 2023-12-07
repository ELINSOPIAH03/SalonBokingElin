package com.booking.service;

import java.util.List;
import java.util.stream.IntStream;

import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrintService {
    public static void printMenu(String title, String[] menuArr){
        int num = 1;
        System.out.println(title);
        for (int i = 0; i < menuArr.length; i++) {
            if (i == (menuArr.length - 1)) {   
                num = 0;
            }
            System.out.println(num + ". " + menuArr[i]);   
            num++;
        }
    }

    public String printServices(List<Service> serviceList){
        StringBuilder result = new StringBuilder();
        for (Service service : serviceList) {
            result.append(service.getServiceName()).append(", ");
        }
        if (result.length() > 2) {
            result.setLength(result.length() - 2); // Menghapus koma terakhir
        }
        return result.toString();
    }

    // Function yang dibuat hanya sebgai contoh bisa disesuaikan kembali
    public void showRecentReservation() {
        List<Reservation> recentReservations = TemporaryReservationStorage.getTemporaryReservations();
        System.out.println("\nData Recent Reservation");
        System.out.printf("| %-4s | %-6s | %-15s | %-20s | %-15s | %-15s | %-10s |\n",
                "No.", "ID", "Nama Customer", "Service","Total", "Pegawai", "Workstage");
        System.out.println("===========================================================================================================");
        
        IntStream.range(0, recentReservations.size())
                .forEach(i -> {
                    Reservation reservation = recentReservations.get(i);
                    System.out.printf("| %-4s | %-6s | %-15s | %-20s | %-15s | %-15s | %-10s |\n",
                            i + 1, reservation.getReservationId(), reservation.getCustomer().getName(),
                            printServices(reservation.getServices()), reservation.getReservationPrice(),
                            reservation.getEmployee().getName(), "In Process");
                });
    
        // Bersihkan data temp setelah ditampilkan (opsional)
        TemporaryReservationStorage.clearTemporaryReservations();
    }

    public void showAllCustomer(List<Person> personList) {
        System.out.println("\nData Customer");
        System.out.printf("| %-3s | %-7s | %-15s | %-15s | %-10s | %-14s |\n", "No", "ID", "Nama", "Alamat", "Membership", "Uang");
        System.out.println("===================================================================================");
        
        IntStream.range(0, personList.size())
                .filter(i -> personList.get(i) instanceof com.booking.models.Customer)
                .forEach(i -> {
                    com.booking.models.Customer customer = (com.booking.models.Customer) personList.get(i);
                    System.out.printf("| %-3d | %-6s | %-15s | %-15s | %-10s | %-10s |\n",
                            i + 1, customer.getId(), customer.getName(), customer.getAddress(),
                            customer.getMember() != null ? customer.getMember().getMembershipName() : "none",
                            formatCurrency(customer.getWallet()));
                });
        
    }
    
    private String formatCurrency(double amount) {
        return String.format("Rp%,.2f", amount);
    }

    public void showAllEmployee(List<Person> personList){
        System.out.println("Data Employee");
        System.out.printf("| %-3s | %-6s | %-15s | %-15s | %-10s |\n", "No", "ID", "Nama", "Alamat", "Pengalaman");
        System.out.println("================================================================");
        IntStream.range(0, personList.size())
                .filter(i -> personList.get(i) instanceof com.booking.models.Employee)
                .forEach(i -> {
                    com.booking.models.Employee employee = (com.booking.models.Employee) personList.get(i);
                    System.out.printf("| %-3d | %-6s | %-15s | %-15s | %-10d |\n",
                            i + 1, employee.getId(), employee.getName(), employee.getAddress(), employee.getExperience());
                });
    }

    // public void showHistoryReservation(List<Reservation> reservationList){
    //     System.out.println("Data History Reservation");
    //     System.out.printf("| %-3s | %-6s | %-15s | %-15s | %-10s |\n", "No", "ID", "Nama Customer", "Service", "Workstage");
    //     System.out.println("=================================================================");
    //     IntStream.range(0, reservationList.size())
    //             .forEach(i -> {
    //                 Reservation reservation = reservationList.get(i);
    //                 System.out.printf("| %-3d | %-6s | %-15s | %-15s | %-10s |\n",
    //                         i + 1, reservation.getReservationId(), reservation.getCustomer().getName(),
    //                         printServices(reservation.getServices()), reservation.getWorkstage());
    //             });
    // }
    public void showHistoryReservation(List<Reservation> reservationList) {
        System.out.println("\nData Reservation History + Total Keuntungan");
        System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s | %-10s |\n",
                "No.", "ID", "Nama Customer", "Service", "Total Biaya", "Workstage");
        System.out.println("=======================================================================");
    
        double totalProfit = 0;
    
        for (int i = 0; i < reservationList.size(); i++) {
            Reservation reservation = reservationList.get(i);
            if (reservation.getWorkstage().equalsIgnoreCase("Finish")) {
                System.out.printf("| %-4d | %-4s | %-11s | %-15s | %-15s | %-10s |\n",
                        i + 1, reservation.getReservationId(), reservation.getCustomer().getName(),
                        printServices(reservation.getServices()), reservation.getReservationPrice(),
                        reservation.getWorkstage());
    
                totalProfit += reservation.getReservationPrice();
            }
        }
    
        System.out.printf("Total Keuntungan: Rp. %,.2f\n", totalProfit);
    }

    public void showAllService(List<Service> serviceList) {
        System.out.println("Tampilan Service");
        System.out.printf("| %-3s | %-7s | %-25s | %-10s |\n",
                "No.", "ID", "Nama Service", "Harga");
        System.out.println("=========================================================");
        serviceList.stream()
                .map(service -> String.format("| %-3d | %-7s | %-25s | %-10.2f |",
                        serviceList.indexOf(service) + 1, service.getServiceId(), service.getServiceName(), service.getPrice()))
                .forEach(System.out::println);
    }
    
    
}
