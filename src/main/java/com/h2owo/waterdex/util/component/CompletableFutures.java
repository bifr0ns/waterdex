package com.h2owo.waterdex.util.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class CompletableFutures {

  @Autowired
  private Waterqualitydata wqd;

  public StringBuilder getWaterQualityInfo(double lat, double lon) {
    List<String> chars = List.of("Temperature, water", "Dissolved oxygen (DO)", "Phosphorus", "Chlorophyll a (probe relative fluorescence)",
            "pH", "Specific conductance", "Dissolved oxygen saturation", "Transparency, tube with disk", "Temperature, air",
            "Flow", "Escherichia coli", "Turbidity", "Nitrate + Nitrite", "Calcium carbonate", "True color", "Iron");

    // Create a list of CompletableFuture tasks
    List<CompletableFuture<StringBuilder>> futures = chars.stream()
            .map(charName -> CompletableFuture.supplyAsync(() -> wqd.getResponse(lat, lon, charName))).toList();

    // Wait for all CompletableFuture tasks to complete
    CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

    StringBuilder info = new StringBuilder();

    try {
      allOf.get(); // Wait for all tasks to complete

      // Extract results from CompletableFuture tasks
      for (CompletableFuture<StringBuilder> future : futures) {
        StringBuilder result = future.get(); // Get the result from each CompletableFuture
        info.append(result);
      }

    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    return info;
  }
}
