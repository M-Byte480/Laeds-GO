package ie.thirdfloor.csis.ul.laedsgo.entities;

import java.time.LocalDateTime;
import java.util.Date;

public class LaedDeckModel implements Comparable<LaedDeckModel> {

private String name;

private String description;

private Integer leadID;

private String model;

private Integer rarity;

private Date timestamp;


public LaedDeckModel(String name, String description, Integer leadID, String model, Integer rarity, Date timestamp){
    this.name = name;
    this.description = description;
    this.leadID = leadID;
    this.model = model;
    this.rarity = rarity;
    this.timestamp = timestamp;
}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLeadID() {
        return leadID;
    }

    public void setLeadID(Integer leadID) {
        this.leadID = leadID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getRarity() {
        return rarity;
    }

    public void setRarity(Integer rarity) {
        this.rarity = rarity;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(LaedDeckModel that) {
        return Integer.compare(
                this.leadID,
                that.leadID
        );
    }
}
