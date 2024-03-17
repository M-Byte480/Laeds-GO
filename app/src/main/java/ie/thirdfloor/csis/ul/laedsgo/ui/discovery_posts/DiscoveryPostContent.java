package ie.thirdfloor.csis.ul.laedsgo.ui.discovery_posts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.thirdfloor.csis.ul.laedsgo.entities.DiscoveryPost;

public class DiscoveryPostContent {
    public static final String[] RANDOM_USERNAMES = {
            "BiscuitNinja42",
            "CaptainFuzzyPants",
            "SirLlamaSocks",
            "BananaMuffinManiac",
            "ProfessorPineapplePants",
            "QueenOfQuackery",
            "DrunkenDonutDude",
            "TickleMonster3000",
            "SassyPantsMcGee",
            "WaffleWizard9000"
    };
    public static final String[] RANDOM_TEXT = {
      "Yo lads just caught a bitch ass Top Mulla-G",
      "Saw a wild Ruski in the snowy plains!!!",
      "THe infinity Drug Dealrer is lurking in Limerick City",
      "AYO! Is that THe Fayer Enough in Cedar House?",
      "The Polish Pokem... I mean Lad can be caught in Darkside",
      "Milanonom spotted @Inver_Castletroy"
    };

    public static final String[] RANDOM_LOCATIONS = {
            "Castletroy",
            "College Court",
            "Brookfield, Block 5, 315",
            "UL Sports Arena",
            "Earth",
            "Putin's Basement"
    };

    public static final int RTL = RANDOM_TEXT.length;
    public static final int RLL = RANDOM_LOCATIONS.length;
    public static final int RUL = RANDOM_USERNAMES.length;

    public static final List<DiscoveryPost> ITEMS = new ArrayList<>();
    public static final Map<String, DiscoveryPost> ITEM_MAP = new HashMap<>();
    private static final int ELEMENT_COUNT = 20;
    private static void addPost(DiscoveryPost post){
        ITEMS.add(post);
        ITEM_MAP.put(post.getId(), post);
    }

    private static DiscoveryPost createPost(int p){
        return new DiscoveryPost(
                p + "",
                p + "",
                RANDOM_USERNAMES[ (int) (Math.random() * RUL)],
                (int) (Math.random() * 1000),
                (int) (Math.random() * 1000),
                false, false,
                RANDOM_TEXT[ (int) (Math.random() * RTL)],
                RANDOM_LOCATIONS[ (int) (Math.random() * RLL)],
                "12:" + (int) (Math.random() * 60));
    }

    static {
        for (int i = 1; i <= ELEMENT_COUNT; i++) {
            addPost(createPost(i));
        }
    }
}
