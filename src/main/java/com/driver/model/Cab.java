package com.driver.model;

import javax.persistence.*;

@Entity
@Table(name = "cab")
public class Cab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    private int perKmRate;

    private boolean avaibale;

    @OneToOne(mappedBy = "cab", cascade = CascadeType.ALL)
    private Driver driver;

    public Cab() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(int perKmRate) {
        this.perKmRate = perKmRate;
    }

    public boolean isAvaibale() {
        return avaibale;
    }

    public void setAvaibale(boolean avaibale) {
        this.avaibale = avaibale;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}