package org.example.Models;

import java.io.Serializable;
import java.util.List;

public class Stage implements Serializable {
    private List<Zone> zones;

    public Stage(List<Zone> zones) {
        this.zones = zones;
    }

    public List<Zone> getZones() {
        return zones;
    }
}
