package com.booking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer extends Person {
    private double wallet;
    private Membership member;

    public void reduceWallet(double amount) {
        if (amount > 0 && amount <= wallet) {
            wallet -= amount;
        } else {
            System.out.println("Jumlah uang yang ditarik tidak valid.");
        }
    }

}
