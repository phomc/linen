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

import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.MobEntity;

public class ClutchBlockFallingGoal extends PlaceBlockGoal {
	public ClutchBlockFallingGoal(MobEntity mob) {
		super(mob);
	}

	public ClutchBlockFallingGoal(MobEntity mob, BlockState state) {
		super(mob, state);
	}

	@Override
	public boolean canStart() {
		return this.mob.getGroundBlockPos().isWithinDistance(this.mob.getBlockPos(), 4);
	}

	@Override
	public void start() {
		this.pos = this.mob.getGroundBlockPos().add(0, 1, 0);
		super.start();
	}
}
