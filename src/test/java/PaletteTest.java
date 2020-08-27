import com.dfsek.betterend.ProbabilityCollection;
import com.dfsek.betterend.world.generation.biomes.BlockPalette;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PaletteTest {
    public static void main(String[] args) {
        long l = System.nanoTime();
        BlockPalette p = new BlockPalette(new Random());
        System.out.println((System.nanoTime() - l)/1000 + "us elapsed (Instantiation)");
        l = System.nanoTime();
        p.add(Material.GRASS_BLOCK, 1);
        System.out.println((System.nanoTime() - l)/1000000 + "ms elapsed (Fill 1)");
        l = System.nanoTime();
        p.add(Material.DIRT, 12);
        System.out.println((System.nanoTime() - l)/1000000 + "ms elapsed (Fill 2)");
        l = System.nanoTime();
        p.add(new ProbabilityCollection<Material>().add(Material.STONE, 1).add(Material.DIRT, 1), 20);
        System.out.println((System.nanoTime() - l)/1000000 + "ms elapsed (Fill 3)");
        l = System.nanoTime();
        p.add(Material.STONE, 30);
        System.out.println((System.nanoTime() - l)/1000000 + "ms elapsed (Fill 4)");
        l = System.nanoTime();
        List<Material> m = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            long l2 = System.nanoTime();
            m.add(p.get(i));
            System.out.println(p.get(i) + " retrieved in " + (System.nanoTime() - l2)/1000 + "us");
        }
        System.out.println((double)(System.nanoTime() - l)/1000000 + "ms elapsed (Getters, raw x10), got " + m.size() + " values");
        for(int i = 0; i < 100000; i++) {
            p.get(i);
        }
        System.out.println((double)(System.nanoTime() - l)/1000000 + "ms elapsed (Getters, raw x100000), got " + 100000 + " values");

        System.out.println();
        System.out.println("Beginning fill for stress-test");
        l = System.nanoTime();
        BlockPalette p2 = new BlockPalette(new Random());
        for(int i = 0; i < 500000; i++) {
            p2.add(Material.DIRT, 1);
            p2.add(Material.STONE, 1);
        }
        System.out.println((System.nanoTime() - l)/1000000 + "ms elapsed (Instantiation/Fill x500000)");
        l = System.nanoTime();
        for(int i = 0; i < 1000000; i++) {
            long l2 = System.nanoTime();
            if(i % 100001 == 0) System.out.println(p2.get(i) + " retrieved in " + (System.nanoTime() - l2)/1000 + "us at layer " + i);
        }
        System.out.println((double)(System.nanoTime() - l)/1000000 + "ms elapsed (Getters, raw x1000000), got " + 1000000 + " values");
    }
}
