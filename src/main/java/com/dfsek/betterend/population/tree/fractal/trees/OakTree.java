package com.dfsek.betterend.population.tree.fractal.trees;

import com.dfsek.betterend.population.tree.fractal.FractalTree;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.Random;


public class OakTree extends FractalTree {
    /**
     * Instantiates a TreeGrower at an origin location.
     *
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     */
    public OakTree(Location origin, Random random) {
        super(origin, random);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow() {
        growBranch(super.getOrigin().clone(), new Vector(super.getRandom().nextInt(5)-2, super.getRandom().nextInt(4)+6, super.getRandom().nextInt(5)-2), 2, 0);
    }

    private void growBranch(Location l1, Vector diff, double d1, int recursions) {
        if(recursions > 1) {
            generateSphere(l1, Material.OAK_LEAVES, 2+super.getRandom().nextInt(2), false);
            if(recursions > 2) return;
        }
        if(diff.getY() < 0) diff.rotateAroundAxis(getPerpendicular(diff.clone()).normalize(), Math.PI);
        System.out.println("Recursion " + recursions);
        int d = (int) diff.length();
        for(int i = 0; i < d; i++) {
            super.generateSphere(l1.clone().add(diff.clone().multiply((double)i/d)), Material.OAK_WOOD, Math.max((int) d1, 0), false);
        }
        double runningAngle = (double) 45 / (recursions + 1);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.7).rotateAroundX(Math.toRadians(runningAngle + getNoise())).rotateAroundZ(Math.toRadians(getNoise())),
                d1-1, recursions+1);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.7).rotateAroundX(Math.toRadians(-runningAngle + getNoise())).rotateAroundZ(Math.toRadians(getNoise())),
                d1-1, recursions+1);
            growBranch(l1.clone().add(diff), diff.clone().multiply(0.7).rotateAroundZ(Math.toRadians(runningAngle + getNoise())).rotateAroundX(Math.toRadians(getNoise())),
                    d1 - 1, recursions + 1);
            growBranch(l1.clone().add(diff), diff.clone().multiply(0.7).rotateAroundZ(Math.toRadians(-runningAngle + getNoise())).rotateAroundX(Math.toRadians(getNoise())),
                    d1 - 1, recursions + 1);
    }

    private int getNoise() {
        return super.getRandom().nextInt(60)-30;
    }
}
