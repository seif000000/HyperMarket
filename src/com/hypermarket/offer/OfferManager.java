package com.hypermarket.offer;

import java.util.ArrayList;
import java.util.List;

public class OfferManager {
    private List<Offer> offers;

    public OfferManager() {
        offers = new ArrayList<>();
    }

    public void addOffer(Offer offer) {
        if (offer.getOfferId() <= 0) {
            System.out.println("Error: Offer ID must be positive!");
            return;
        }
        if (offer.getDiscount() < 0 || offer.getDiscount() > 100) {
            System.out.println("Error: Discount must be between 0 and 100!");
            return;
        }
        offers.add(offer);
        System.out.println("Offer added successfully!");
    }

    public void listOffers() {
        if (offers.isEmpty()) {
            System.out.println("No offers available!");
        } else {
            System.out.println("=== Offers List ===");
            for (Offer offer : offers) {
                System.out.println(offer);
            }
        }
    }

    public List<Offer> getAllOffers() {
        return List.of();
    }
}