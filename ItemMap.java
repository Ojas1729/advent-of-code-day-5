import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class ItemMap {
    ArrayList<ItemMapEntry> items;
    long[] seeds;
  
    private long translate(long itemnum, ItemType from, ItemType to) {
        for (ItemMapEntry item: this.items) {
            if (item.from == from && item.to == to) {
                if (item.map(itemnum) != -1) {
                    return item.map(itemnum);
                } 
            }
        }
        return itemnum;
    }

    
    
    private void loadFile(String filename) throws Exception {
        BufferedReader b = new BufferedReader(new FileReader(filename));
        String[] header = b.readLine().split(" ");
        this.seeds = new long[header.length];
        for (int i = 1; i < header.length; i++) {
            seeds[i-1] = Long.parseLong(header[i]);
        }
        ItemType curFrom = null;
        ItemType curTo = null;
        while (b.ready()) {
            String s = b.readLine();
            //System.out.println("Line:" + s);
            if (s.length() == 0) {
                continue;
            }
            if (s.charAt(0) >= '0' && s.charAt(0) <= '9') {
                items.add(new ItemMapEntry(s, curFrom, curTo));
            }
            else {
                ItemType[] res = ItemType.pairFromString(s);
                curFrom = res[0];
                curTo = res[1];
                //System.out.println(curFrom + " " + curTo);
            }
        }
    }

    public long solve() {
        int depth = 8;
        long res_min = -1;
        ItemType[] order = {
            ItemType.Seed,
            ItemType.Soil,
            ItemType.Fertilizer,
            ItemType.Water,
            ItemType.Light,
            ItemType.Temprature,
            ItemType.Humidity,
            ItemType.Location
        };
        for (long seed: seeds) {
            long res = seed;
            for (int i = 0; i < depth-1; i++) {
                res = translate(res, order[i], order[i+1]);
                System.out.println(res);
            }
            if (res_min == -1) {
                res_min = res;
            }
            if (res < res_min) {
                res_min = res;
            }
        }
        return res_min;
    }
    
    public ItemMap(String filename) {
        this.items = new ArrayList<>();
        try {
            this.loadFile(filename);
        }
        catch (Exception e) {
            System.out.println("Error in opening file" + e);
            System.exit(0);
        }
    }

    
    public static void main(String[] args) {
        System.out.println("Hello");
        ItemMapEntry a = new ItemMapEntry("1 2 3", ItemType.Seed, ItemType.Soil);
        System.out.println(a);
        ItemMap x = new ItemMap("input");
        System.out.println(x.solve());
    }
}

enum ItemType {
    Seed,
    Soil,
    Fertilizer,
    Water,
    Light,
    Temprature,
    Humidity,
    Location;

    public static ItemType[] pairFromString(String s) {
        // s: "item1-to-item2 map:" 
        try {
        ItemType[] res = new ItemType[2];
        String[] items = s.split(" ")[0].split("-to-");
        res[0] = ItemType.fromString(items[0]);
        res[1] = ItemType.fromString(items[1]);
        return res; 
        }
        catch (Exception e){
            System.out.println("Incorrect syntax: " + s);
            System.exit(0);
        }
        return null;
    }

    private static ItemType fromString(String s) {
        return switch (s) {
            case "seed" -> ItemType.Seed;
            case "soil" -> ItemType.Soil;
            case "fertilizer" -> ItemType.Fertilizer;
            case "water" -> ItemType.Water;
            case "light" -> ItemType.Light;
            case "temperature" -> ItemType.Temprature;
            case "humidity" -> ItemType.Humidity;
            case "location" -> ItemType.Location;
            default -> null;
        };
    }
}

class ItemMapEntry {
    ItemType from;
    ItemType to;
    long source;
    long destination;
    long length;

    public ItemMapEntry(String s, ItemType from, ItemType to) {
        if (from == null || to == null) {
            System.out.println("Something went wrong!");
            System.exit(0);
        }
        this.from = from;
        this.to = to;
        String[] items = s.split(" ");
        this.destination = Long.parseLong(items[0]);
        this.source = Long.parseLong(items[1]);
        this.length = Long.parseLong(items[2]);
    }

    public long map(long num) {
        if (num - this.source > 0 && num - this.source < this.length) {
            return this.destination + (num - this.source);
        }
        return -1;
    }

    public String toString() {
        return from + " -> " + to + " " + "(" + source + " " + destination + " " + length+ ")";
    }
}
