package com.injir.create_brass_coated.blocks.drill;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.actors.BlockBreakingMovementBehaviour;
import com.simibubi.create.content.contraptions.components.actors.DrillActorInstance;
import com.simibubi.create.content.contraptions.components.actors.DrillBlock;
import com.simibubi.create.content.contraptions.components.actors.DrillRenderer;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementContext;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ActorInstance;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionRenderDispatcher;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BrassDrillMovementBehaviour extends BlockBreakingMovementBehaviour {

	@Override
	public boolean isActive(MovementContext context) {
		return !VecHelper.isVecPointingTowards(context.relativeMotion, context.state.getValue(BrassDrillBlock.FACING)
			.getOpposite());
	}

	@Override
	public Vec3 getActiveAreaOffset(MovementContext context) {
		return Vec3.atLowerCornerOf(context.state.getValue(BrassDrillBlock.FACING)
			.getNormal()).scale(.65f);
	}

	@Override
	@OnlyIn(value = Dist.CLIENT)
	public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld,
		ContraptionMatrices matrices, MultiBufferSource buffer) {
        if (!ContraptionRenderDispatcher.canInstance())
			DrillRenderer.renderInContraption(context, renderWorld, matrices, buffer);
	}

	@Override
	public boolean hasSpecialInstancedRendering() {
		return true;
	}

	@Nullable
	@Override
	public ActorInstance createInstance(MaterialManager materialManager, VirtualRenderWorld simulationWorld, MovementContext context) {
		return new BrassDrillActorInstance(materialManager, simulationWorld, context);
	}

	@Override
	protected DamageSource getDamageSource() {
		return BrassDrillBlock.damageSourceBrassDrill;
	}

	@Override
	public boolean canBreak(Level world, BlockPos breakingPos, BlockState state) {
		return super.canBreak(world, breakingPos, state) && !state.getCollisionShape(world, breakingPos)
			.isEmpty() && !AllBlocks.TRACK.has(state);
	}

}
