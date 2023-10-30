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
package dev.phomc.linen.test.base;

import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

import dev.phomc.linen.api.autoregister.AutoRegister;

@AutoRegister("linen_test")
public final class TestAutoRegister {
	public static final Item TEST_ITEM = new Item(new FabricItemSettings());
	public static final PickaxeItem TEST_PICKAXE = new PickaxeItem(ToolMaterials.IRON, 1, 1, new FabricItemSettings());
}
