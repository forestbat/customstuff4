package cubex2.cs4.plugins.vanilla.crafting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cubex2.cs4.util.ItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Map;

public class MachineManager
{
    private static final Map<ResourceLocation, MachineManager> instances = Maps.newHashMap();

    private final List<MachineRecipe> recipes = Lists.newArrayList();
    private final List<MachineFuel> fuels = Lists.newArrayList();

    public static MachineManager getInstance(ResourceLocation list)
    {
        if (!instances.containsKey(list))
        {
            instances.put(list, new MachineManager());
        }

        return instances.get(list);
    }

    public static List<MachineRecipe> getRecipes(ResourceLocation list)
    {
        if (list.toString().equals("minecraft:vanilla"))
        {
            throw new UnsupportedOperationException();
        } else
        {
            return getInstance(list).recipes;
        }
    }

    public static void addRecipe(ResourceLocation list, MachineRecipe recipe)
    {
        getRecipes(list).add(recipe);
    }

    public static boolean isPartOfRecipe(ResourceLocation list, ItemStack stack)
    {
        if (stack.isEmpty())
            return false;

        if (list.toString().equals("minecraft:vanilla"))
        {
            return !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty();
        }

        for (MachineRecipe recipe : getInstance(list).recipes)
        {
            if (recipe.getRecipeInput().stream()
                      .anyMatch(input -> ItemHelper.stackMatchesRecipeInput(stack, input, false)))
                return true;
        }

        return false;
    }

    public static MachineRecipe findMatchingRecipe(ResourceLocation list, NonNullList<ItemStack> input, List<FluidStack> inputFluid, World worldIn)
    {
        if (list.toString().equals("minecraft:vanilla"))
        {
            if (input.size() == 1 && !input.get(0).isEmpty())
            {
                return new VanillaFurnaceRecipe(FurnaceRecipes.instance().getSmeltingResult(input.get(0)));
            } else
            {
                return MachineRecipe.EMPTY;
            }
        }

        return findMatchingRecipe(getRecipes(list), input, inputFluid, worldIn);
    }

    public static MachineRecipe findMatchingRecipe(List<MachineRecipe> recipes, NonNullList<ItemStack> input, List<FluidStack> inputFluid, World worldIn)
    {
        for (MachineRecipe recipe : recipes)
        {
            if (input.size() == recipe.getInputStacks()
                && inputFluid.size() == recipe.getFluidStacks()
                && recipe.matches(input, inputFluid, worldIn))
            {
                return recipe;
            }
        }

        return MachineRecipe.EMPTY;
    }

    public static void addFuel(ResourceLocation list, MachineFuel fuel)
    {
        if (list.toString().equals("minecraft:vanilla"))
        {
            throw new UnsupportedOperationException();
        } else
        {
            getInstance(list).fuels.add(fuel);
        }
    }

    public static MachineFuel findMatchingFuel(ResourceLocation list, NonNullList<ItemStack> input)
    {
        if (list.toString().equals("minecraft:vanilla"))
        {
            if (input.size() == 1 && !input.get(0).isEmpty())
            {
                ItemStack stack = input.get(0);
                int burnTime = TileEntityFurnace.getItemBurnTime(stack);
                if (burnTime > 0)
                    return new VanillaFurnaceFuel(stack, burnTime);
            }

            return MachineFuel.EMPTY;
        }

        return findMatchingFuel(getInstance(list).fuels, input);
    }

    public static MachineFuel findMatchingFuel(List<MachineFuel> fuels, NonNullList<ItemStack> input)
    {
        for (MachineFuel fuel : fuels)
        {
            if (input.size() == fuel.getFuelInput().size() && fuel.matches(input))
            {
                return fuel;
            }
        }

        return MachineFuel.EMPTY;
    }

    /*public static int getBurnTime(ResourceLocation list, NonNullList<ItemStack> items)
    {
        if (list.toString().equals("minecraft:vanilla"))
        {
            if (items.size() == 1)
            {
                return TileEntityFurnace.getItemBurnTime(items.get(0));
            } else
            {
                return 0;
            }
        }

        for (MachineFuel fuel : getInstance(list).fuels)
        {
            int burnTime = fuel.getBurnTime(items);
            if (burnTime > 0)
                return burnTime;
        }

        return 0;
    }*/

    public static boolean isPartOfFuel(ResourceLocation list, ItemStack stack)
    {
        if (stack.isEmpty())
            return false;

        if (list.toString().equals("minecraft:vanilla"))
        {
            return TileEntityFurnace.getItemBurnTime(stack) > 0;
        }

        for (MachineFuel fuel : getInstance(list).fuels)
        {
            if (fuel.getFuelInput().stream()
                    .anyMatch(input -> ItemHelper.stackMatchesRecipeInput(stack, input, false)))
                return true;
        }

        return false;
    }
}
