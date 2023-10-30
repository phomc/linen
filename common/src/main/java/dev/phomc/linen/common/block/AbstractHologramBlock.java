/*
 * Copyright (c) 2023 PhoMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.phomc.linen.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import dev.phomc.linen.common.block.entity.AbstractHologramBlockEntity;

public abstract class AbstractHologramBlock extends BlockWithEntity {
	protected AbstractHologramBlock() {
		super(FabricBlockSettings.copyOf(Blocks.BARRIER).noCollision());
	}

	protected AbstractHologramBlock(Settings settings) {
		super(settings);
	}

	private void updateHologramItem(BlockState state, World world, BlockPos pos, PlayerEntity entity) {
		var blockEntity = world.getBlockEntity(pos);

		if (blockEntity instanceof AbstractHologramBlockEntity hologramBlockEntity) {
			hologramBlockEntity.updateHologramItem(entity.getMainHandStack());
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient()) return ActionResult.SUCCESS;

		if (player.isCreative() && !player.getMainHandStack().isEmpty()) {
			this.updateHologramItem(state, world, pos, player);
			return ActionResult.CONSUME;
		}

		return ActionResult.PASS;
	}

	@Override
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		return stateFrom.isOf(this);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.cuboid(1 / 8f, 0f, 1 / 8f, 7 / 8f, 1.0f, 7 / 8f);
	}

	@Override
	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}

	@Override
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 1f;
	}
}
