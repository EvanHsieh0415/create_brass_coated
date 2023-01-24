package com.injir.create_brass_coated.blocks.girder;

import com.injir.create_brass_coated.blocks.BrassPartials;
import com.simibubi.create.foundation.block.connected.CTModel;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap.Builder;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BrassConnectedGirderModel extends CTModel {

	protected static ModelProperty<ConnectionData> BRASS_CONNECTION_PROPERTY = new ModelProperty<>();

	public BrassConnectedGirderModel(BakedModel originalModel) {
		super(originalModel, new BrassGirderCTBehaviour());
	}

	@Override
	protected Builder gatherModelData(Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state) {
		ConnectionData connectionData = new ConnectionData();
		for (Direction d : Iterate.horizontalDirections)
			connectionData.setConnected(d, BrassGirderBlock.isConnected(world, pos, state, d));
		return super.gatherModelData(builder, world, pos, state).withInitial(BRASS_CONNECTION_PROPERTY, connectionData);
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData) {
		List<BakedQuad> superQuads = super.getQuads(state, side, rand, extraData);
		if (side != null || !extraData.hasProperty(BRASS_CONNECTION_PROPERTY))
			return superQuads;
		List<BakedQuad> quads = new ArrayList<>(superQuads);
		ConnectionData data = extraData.getData(BRASS_CONNECTION_PROPERTY);
		for (Direction d : Iterate.horizontalDirections)
			if (data.isConnected(d))
				quads.addAll(BrassPartials.BRASS_GIRDER_BRACKETS.get(d)
					.get()
					.getQuads(state, side, rand, extraData));
		return quads;
	}

	private class ConnectionData {
		boolean[] connectedFaces;

		public ConnectionData() {
			connectedFaces = new boolean[4];
			Arrays.fill(connectedFaces, false);
		}

		void setConnected(Direction face, boolean connected) {
			connectedFaces[face.get2DDataValue()] = connected;
		}

		boolean isConnected(Direction face) {
			return connectedFaces[face.get2DDataValue()];
		}
	}

}
