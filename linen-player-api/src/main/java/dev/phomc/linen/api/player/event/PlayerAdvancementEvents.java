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
package dev.phomc.linen.api.player.event;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.server.network.ServerPlayerEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class PlayerAdvancementEvents {
	public static final Event<GrantCriterion> GRANT_CRITERION = EventFactory.createArrayBacked(GrantCriterion.class, callbacks -> (player, advancementEntry, criterionKey) -> {
		for (GrantCriterion callback : callbacks) {
			callback.grantCriterion(player, advancementEntry, criterionKey);
		}
	});


	@FunctionalInterface
	public interface GrantCriterion {
		void grantCriterion(ServerPlayerEntity player, AdvancementEntry advancementEntry, String criterionKey);
	}
}