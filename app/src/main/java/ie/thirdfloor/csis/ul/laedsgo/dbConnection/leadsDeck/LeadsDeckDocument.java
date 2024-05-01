package ie.thirdfloor.csis.ul.laedsgo.dbConnection.leadsDeck;

import java.util.Date;

import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.entities.LaedDeckModel;

public class LeadsDeckDocument implements IDocument, Comparable<LeadsDeckDocument> {
    public Integer leadId = 0;
    public String name = "";
    public String description = "";
    public String model = "";
    public String picture = "";
    public Integer rarity = 0;
    public Date timestamp;

    @Override
    public String toString() {
        return "LeadsDeckDocument{" +
                "leadId=" + leadId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", model='" + model + '\'' +
                ", picture='" + picture + '\'' +
                ", rarity=" + rarity +
                ", timestamp=" + timestamp +
                '}';
    }

    public int compareTo(LeadsDeckDocument lad) {
        return 0;
    }
}
