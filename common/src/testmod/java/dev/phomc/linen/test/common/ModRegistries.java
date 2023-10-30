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
package dev.phomc.linen.test.common;

import net.minecraft.block.entity.BlockEntityType;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import dev.phomc.linen.api.autoregister.AutoRegister;
import dev.phomc.linen.test.common.block.TestHologramBlock;
import dev.phomc.linen.test.common.block.entity.TestHologramBlockEntity;

@AutoRegister("linen_test")
public final class ModRegistries {
	public static final TestHologramBlock TEST_HOLOGRAM_BLOCK = new TestHologramBlock();
	public static final BlockEntityType<TestHologramBlockEntity> TEST_HOLOGRAM_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(TestHologramBlockEntity::new, TEST_HOLOGRAM_BLOCK).build();
}
