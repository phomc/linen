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
package dev.phomc.linen.common.block.entity;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractHologramBlockEntity extends BlockEntity {
	private static final String HOLOGRAM_ITEM_KEY = "HologramItem";

	private ItemStack hologramItem = Items.BARRIER.getDefaultStack();

	public AbstractHologramBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void updateHologramItem(ItemStack item) {
		this.hologramItem = item;
		this.updateListeners();
	}

	public ItemStack getHologramItem() {
		return this.hologramItem.isEmpty() ? Items.BARRIER.getDefaultStack() : this.hologramItem;
	}

	private void updateListeners() {
		this.markDirty();
		this.world.updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
	}

	public boolean isSpinning() {
		return false;
	}

	public float getScale() {
		return 1f;
	}

	@NotNull
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return this.createNbt();
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		this.hologramItem = ItemStack.fromNbt(nbt.getCompound(HOLOGRAM_ITEM_KEY));
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		nbt.put(HOLOGRAM_ITEM_KEY, this.hologramItem.writeNbt(new NbtCompound()));
	}
}
