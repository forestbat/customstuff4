package cubex2.cs4.plugins.vanilla.block;

import cubex2.cs4.plugins.vanilla.ContentBlockWall;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Bootstrap;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class BlockWallTest
{
    @BeforeClass
    public static void setUp()
    {
        Bootstrap.register();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testProperties()
    {
        ContentBlockWall content = new ContentBlockWall();
        content.id = "test_getSubtype";

        Block block = content.createBlock();
        Collection<IProperty<?>> properties = block.getBlockState().getProperties();
        assertEquals(5, properties.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_getSubtype()
    {
        ContentBlockWall content = new ContentBlockWall();
        content.id = "test_getSubtype";

        Block block = content.createBlock();
        assertEquals(0, block.getMetaFromState(block.getDefaultState()));
    }
}