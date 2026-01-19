package net.vaultedmc.donationalerts.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PendingAlert {

    private final UUID playerId;

    private final HashMap<String, Integer> items = new HashMap<>();

    @Setter
    private long lastUpdate = System.currentTimeMillis();

    public void addItem(@NonNull String itemName) {
        items.put(itemName, items.getOrDefault(itemName, 0) + 1);
    }

}
