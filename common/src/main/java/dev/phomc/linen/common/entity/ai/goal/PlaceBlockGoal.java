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
package dev.phomc.linen.common.entity.ai.goal;

import java.util.EnumSet;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class PlaceBlockGoal extends Goal {
	protected final MobEntity mob;

	protected BlockPos pos;
	protected BlockState state = Blocks.STONE.getDefaultState();

	public PlaceBlockGoal(MobEntity mob) {
		this.mob = mob;
		this.setControls(EnumSet.of(Control.LOOK, Control.JUMP));
	}

	public PlaceBlockGoal(MobEntity mob, BlockPos pos) {
		this(mob);
		this.pos = pos;
	}

	public PlaceBlockGoal(MobEntity mob, BlockState state) {
		this(mob);
		this.state = state;
	}

	public PlaceBlockGoal(MobEntity mob, BlockPos pos, BlockState state) {
		this(mob);
		this.pos = pos;
		this.state = state;
	}

	@Override
	public void start() {
		if (pos == null) return;

		mob.equipStack(EquipmentSlot.MAINHAND, state.getBlock().asItem().getDefaultStack());

		if (this.canPlaceBlock()) {
			mob.getLookControl().lookAt(pos.getX(), pos.getY(), pos.getZ());

			if (pos.getY() >= mob.getY() && pos.getY() < mob.getEyeY()) {
				mob.getJumpControl().setActive();
			}

			mob.swingHand(Hand.MAIN_HAND);
			mob.getWorld().setBlockState(pos, state);

			var blockSoundGroup = state.getSoundGroup();
			mob.getWorld().playSound(
					this.mob,
					this.pos,
					state.getSoundGroup().getPlaceSound(),
					SoundCategory.BLOCKS,
					(blockSoundGroup.getVolume() + 1.0F) / 2.0F,
					blockSoundGroup.getPitch() * 0.8F
			);
		}
	}

	private boolean canPlaceBlock() {
		var world = mob.getWorld();
		var state = world.getBlockState(pos);

		if (!pos.isWithinDistance(this.mob.getBlockPos(), 5)) return false;
		if (state.isSolidBlock(world, pos)) return false;

		for (var direction : Direction.values()) {
			var neighborPos = pos.offset(direction);

			if (world.getBlockState(neighborPos).isSolidBlock(world, neighborPos)) {
				return true;
			}
		}

		return false;
	}
}
