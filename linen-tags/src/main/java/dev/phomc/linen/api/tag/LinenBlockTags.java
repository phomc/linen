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
package dev.phomc.linen.api.tag;

import net.minecraft.block.Block;
import net.minecraft.registry.tag.TagKey;

import net.fabricmc.fabric.impl.tag.convention.TagRegistration;

public final class LinenBlockTags {
	public static final TagKey<Block> GREEN = register("green");

	private LinenBlockTags() {
	}

	private static TagKey<Block> register(String tagID) {
		return TagRegistration.BLOCK_TAG_REGISTRATION.registerCommon(tagID);
	}
}
