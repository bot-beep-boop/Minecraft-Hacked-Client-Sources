package net.minecraft.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;

public enum EnumCreatureType
{
    MONSTER("MONSTER", 0, IMob.class, 70, Material.air, false, false),
    CREATURE("CREATURE", 1, EntityAnimal.class, 10, Material.air, true, true),
    AMBIENT("AMBIENT", 2, EntityAmbientCreature.class, 15, Material.air, true, false),
    WATER_CREATURE("WATER_CREATURE", 3, EntityWaterMob.class, 5, Material.water, true, false);

    /**
     * The root class of creatures associated with this EnumCreatureType (IMobs for aggressive creatures, EntityAnimals
     * for friendly ones)
     */
    private final Class creatureClass;
    private final int maxNumberOfCreature;
    /** A flag indicating whether this creature type is peaceful. */
    private final boolean isPeacefulCreature;

    /** Whether this creature type is an animal. */
    private final boolean isAnimal; 

    private EnumCreatureType(String p_i1596_1_, int p_i1596_2_, Class creatureClassIn, int maxNumberOfCreatureIn, Material creatureMaterialIn, boolean isPeacefulCreatureIn, boolean isAnimalIn)
    {
        this.creatureClass = creatureClassIn;
        this.maxNumberOfCreature = maxNumberOfCreatureIn;
        this.isPeacefulCreature = isPeacefulCreatureIn;
        this.isAnimal = isAnimalIn;
    }

    public Class getCreatureClass()
    {
        return this.creatureClass;
    }

    public int getMaxNumberOfCreature()
    {
        return this.maxNumberOfCreature;
    }

    /**
     * Gets whether or not this creature type is peaceful.
     */
    public boolean getPeacefulCreature()
    {
        return this.isPeacefulCreature;
    }

    /**
     * Return whether this creature type is an animal.
     */
    public boolean getAnimal()
    {
        return this.isAnimal;
    }
}