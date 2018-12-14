package jaredbgreat.climaticbiome.proxy;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy implements IProxy {

	@Override
	public void registerItemRender(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, 
				new ModelResourceLocation(item.getRegistryName(), id));
	}

	@Override
	public void fixRenders(BlockLeaves in) {
		in.setGraphicsLevel(Minecraft.getMinecraft().isFancyGraphicsEnabled());
	}
	
	
	@Override
	public void registerGateRenders(BlockFenceGate gate) {
		ModelLoader.setCustomStateMapper(gate,
				(new StateMap.Builder()).ignore(new IProperty[] { BlockFenceGate.POWERED }).build());
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(gate), 0,
				new ModelResourceLocation(gate.getRegistryName().toString(), "inventory"));
		
	}

	
	@Override
	public void output(Exception e) {
		System.err.println();
		System.err.println("*********************");
		System.err.println("*  I AM THE CLIENT  *");
		System.err.println("*********************");
		System.err.println();
		if(e != null) {
			e.printStackTrace();
		}		
	}

	@Override
	public void preInit() {/*Do Nothing*/}

	@Override
	public void init() {/*Do Nothing*/}

}
