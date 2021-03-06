package com.github.revival.common.entity.mob;

import com.github.revival.Revival;
import com.github.revival.common.entity.ai.*;
import com.github.revival.common.enums.EnumDinoType;
import com.github.revival.common.handler.LocalizationStrings;
import com.github.revival.common.item.FAItemRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntitySarcosuchus extends EntitySwimmingDino
{
    public static final double baseHealth = EnumDinoType.Sarcosuchus.Health0;
    public static final double baseDamage = EnumDinoType.Sarcosuchus.Strength0;
    public static final double baseSpeed = EnumDinoType.Sarcosuchus.Speed0;
    public static final double maxHealth = EnumDinoType.Sarcosuchus.HealthMax;
    public static final double maxDamage = EnumDinoType.Sarcosuchus.StrengthMax;
    public static final double maxSpeed = EnumDinoType.Sarcosuchus.SpeedMax;
    public boolean isTamed = false;
    public int WeakToDeath = 0;
    private WaterDinoAIWander aiWaterDinoWander = new WaterDinoAIWander(this, 1.0D);
    private WaterDinoAIHunt aiWaterDinoHunt = new WaterDinoAIHunt(this, EntityLiving.class, 500, false, 0.002D);
    private WaterDinoAIEat aiWaterDinoEat = new WaterDinoAIEat(this, 50, 0.0017D);
    private DinoAIEat aiDinoEat = new DinoAIEat(this, 20);

    public EntitySarcosuchus(World var1)
    {
        super(var1, EnumDinoType.Sarcosuchus);
        this.updateSize();
        /*
         * EDIT VARIABLES PER DINOSAUR TYPE
		 */
        this.adultAge = EnumDinoType.Sarcosuchus.AdultAge;
        // Set initial size for hitbox. (length/width, height)
        this.setSize(4.0F, 1.0F);
        // Size of dinosaur at day 0.
        this.minSize = 0.2F;
        // Size of dinosaur at age Adult.
        this.maxSize = 1.3F;

        this.getNavigator().setCanSwim(true);
        this.tasks.addTask(6, this.aiWaterDinoWander);
        this.tasks.addTask(5, this.aiWaterDinoEat);
        this.tasks.addTask(5, this.aiWaterDinoHunt);
        this.tasks.addTask(8, new EntityAISwimming(this));
        this.tasks.addTask(4, new DinoAIAttackOnCollide(this, 1.1D, true));
        this.tasks.addTask(5, new DinoAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(6, new DinoAIWander(this, 1.0D));
        this.tasks.addTask(7, new DinoAIEat(this, 100));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        tasks.addTask(1, new DinoAIRideGround(this, 1));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new DinoAITargetNonTamedExceptSelfClass(this, EntityLiving.class, 750, false));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
                .setBaseValue(EnumDinoType.Sarcosuchus.Speed0);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
                .setBaseValue(EnumDinoType.Sarcosuchus.Health0);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
                .setBaseValue(EnumDinoType.Sarcosuchus.Strength0);
    }

    @Override
    public boolean isAIEnabled()
    {
        return !this.isModelized() && !this.isWeak();
    }

    protected String getLivingSound()
    {
        if (isChild())
        {
            return "fossil:sarcosuchus_baby_living";
        }
        return "fossil:sarcosuchus_living";
    }

    public void moveEntityWithHeading(float par1, float par2)
    {
        if (this.isWeak())
        {
            this.motionX *= 0.0D;
            this.motionZ *= 0.0D;
            this.rotationPitch = this.rotationYaw = 0;
        }
        else
        {
            super.moveEntityWithHeading(par1, par2);
        }
    }

	/*
	 * Returns the texture's file path as a String.
	 */

    @Override
    public String getTexture()
    {
        if (this.isModelized())
        {
            return super.getTexture();
        }
        if (this.isWeak())
        {
            switch (this.getSubSpecies())
            {
                default:
                    return "fossil:textures/mob/Sarcosuchus_Weak.png";
                case 1:
                    return "fossil:textures/mob/Sarcosuchus/Sarcosuchus_Swamp_Weak.png";
                case 2:
                    return "fossil:textures/mob/Sarcosuchus/Sarcosuchus_Desert_Weak.png";
            }
        }
        else if (this.isTamed())
        {
            switch (this.getSubSpecies())
            {
                default:
                    return "fossil:textures/mob/Sarcosuchus_tame.png";
                case 1:
                    return "fossil:textures/mob/Sarcosuchus/Sarcosuchus_Swamp_tame.png";
                case 2:
                    return "fossil:textures/mob/Sarcosuchus/Sarcosuchus_Desert_tame.png";
            }
        }
        else if (this.isAdult())
        {
            switch (this.getSubSpecies())
            {
                default:
                    return "fossil:textures/mob/Sarcosuchus_Wild.png";
                case 1:
                    return "fossil:textures/mob/Sarcosuchus/Sarcosuchus_Swamp_Wild.png";
                case 2:
                    return "fossil:textures/mob/Sarcosuchus/Sarcosuchus_Desert_Wild.png";
            }
        }
        else if (this.isChild())
        {
            switch (this.getSubSpecies())
            {
                default:
                    return "fossil:textures/mob/Sarcosuchus_baby.png";
                case 1:
                    return "fossil:textures/mob/Sarcosuchus/Sarcosuchus_Swamp_baby.png";
                case 2:
                    return "fossil:textures/mob/Sarcosuchus/Sarcosuchus_Desert_baby.png";
            }
        }


        return "fossil:textures/mob/Sarcosuchus_Wild.png";

    }

    public boolean isWeak()
    {
        return (this.getHealth() < 8) && (this.getDinoAge() >= this.adultAge)
                && !this.isTamed();
    }

    public int getVerticalFaceSpeed()
    {
        return this.isSitting() ? 70 : super.getVerticalFaceSpeed();
    }

	/*
	 * Called for when a player interacts with a dinosaur Specificly for now
	 * when a player tames the dinosaur.
	 */

    public boolean interact(EntityPlayer player)
    {
        super.interact(player);
        ItemStack itemStack = player.inventory.getCurrentItem();
        if (itemStack != null)
        {
            if (itemStack.getItem() == FAItemRegistry.AquaticScarabGem)
            {
                if (!this.isTamed() && this.isWeak())
                {
                    this.heal(200);
                    this.increaseHunger(500);
                    this.setTamed(true);
                    setPathToEntity(null);
                    setAttackTarget(null);
                    this.setOwner(player.getUniqueID().toString());
                    this.setOwnerDisplayName(player.getCommandSenderName());
                    --itemStack.stackSize;

                    if (itemStack.stackSize <= 0)
                    {
                        player.inventory.setInventorySlotContents(
                                player.inventory.currentItem, (ItemStack) null);
                    }

                    return true;
                }
                else
                {
                    if (!this.isWeak())
                    {
                        if (!this.worldObj.isRemote)
                        {
                            Revival.ShowMessage(
                                    StatCollector
                                            .translateToLocal(LocalizationStrings.STATUS_GEM_ERROR_HEALTH),
                                    player);
                        }
                    }

                    if (!this.isAdult())
                    {
                        if (!this.worldObj.isRemote)
                        {
                            Revival.ShowMessage(
                                    StatCollector
                                            .translateToLocal(LocalizationStrings.STATUS_GEM_ERROR_YOUNG),
                                    player);
                        }
                    }
                }
            }

            if (!Revival.DebugMode())
            {
                if (itemStack.getItem() == FAItemRegistry.chickenEss)
                {
                    if (!this.worldObj.isRemote)
                    {
                        Revival.ShowMessage(StatCollector.translateToLocal(LocalizationStrings.STATUS_ESSENCE_FAIL), player);
                        return true;
                    }
                }
            }

            return true;
        }
        return false;
    }

    public boolean isOnSurface()
    {
        return this.worldObj.isAirBlock((int) Math.floor(this.posX), (int) Math.floor(this.posY + (double) (this.getEyeHeight() / 2.0F)), (int) Math.floor(this.posZ));
    }

    public boolean canBreatheUnderwater()
    {
        return true;
    }

    public EntitySarcosuchus spawnBabyAnimal(EntityAgeable var1)
    {
        return new EntitySarcosuchus(this.worldObj);
    }

    public float getEyeHeight()
    {
        return 2.0F + (float) this.getDinoAge() / 1.8F;
    }

    public float getHalfHeight()
    {
        return this.getEyeHeight() / 2.0F - 1.5F;
    }

    public float getMountHeight()
    {
        return this.height / 1.5f;
    }

    public void updateRiderPosition()
    {
        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.setPosition(
                    this.posX,
                    this.posY + this.getMountHeight()
                            + this.riddenByEntity.getYOffset(), this.posZ);
        }
    }

	/*
	 * This gets called when a dinosaur grows naturally or through Chicken
	 * Essence.
	 */

    @Override
    public void updateSize()
    {
        double healthStep;
        double attackStep;
        double speedStep;
        healthStep = (this.maxHealth - this.baseHealth) / (this.adultAge + 1);
        attackStep = (this.maxDamage - this.baseDamage) / (this.adultAge + 1);
        speedStep = (this.maxSpeed - this.baseSpeed) / (this.adultAge + 1);

        if (this.getDinoAge() <= this.adultAge)
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
                    .setBaseValue(
                            Math.round(this.baseHealth
                                    + (healthStep * this.getDinoAge())));
            this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
                    .setBaseValue(
                            Math.round(this.baseDamage
                                    + (attackStep * this.getDinoAge())));
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
                    .setBaseValue(
                            this.baseSpeed + (speedStep * this.getDinoAge()));

            if (this.isTeen())
            {
                this.getEntityAttribute(
                        SharedMonsterAttributes.knockbackResistance)
                        .setBaseValue(0.5D);
            }
            else if (this.isAdult())
            {
                this.getEntityAttribute(
                        SharedMonsterAttributes.knockbackResistance)
                        .setBaseValue(2.0D);
            }
            else
            {
                this.getEntityAttribute(
                        SharedMonsterAttributes.knockbackResistance)
                        .setBaseValue(0.0D);
            }
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public EntityAgeable createChild(EntityAgeable var1)
    {
        EntitySarcosuchus baby = new EntitySarcosuchus(this.worldObj);
        baby.setSubSpecies(this.getSubSpecies());
        return baby;
    }


}
