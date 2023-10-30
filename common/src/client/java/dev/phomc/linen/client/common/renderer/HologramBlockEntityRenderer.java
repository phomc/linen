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
package dev.phomc.linen.client.common.renderer;

import org.joml.Quaternionf;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import dev.phomc.linen.common.block.entity.AbstractHologramBlockEntity;

public class HologramBlockEntityRenderer<T extends AbstractHologramBlockEntity> implements BlockEntityRenderer<T> {
	private static final int WHITE = 0xFFFFFF;
	private static final int BACKGROUND = 0x4F000000;

	protected final BlockEntityRendererFactory.Context ctx;

	public HologramBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		this.ctx = ctx;
	}

	@Override
	public void render(AbstractHologramBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var stack = entity.getHologramItem();

		if (stack.hasGlint()) {
			light = LightmapTextureManager.pack(15, 15);
		}

		matrices.push();
		matrices.translate(.5f, .5f, .5f);
		matrices.push();

		if (entity.isSpinning()) {
			matrices.multiply(new Quaternionf().rotateXYZ(0f, 3.14f * (entity.getWorld().getTime() + tickDelta) / 100f, 0f));
		}

		ctx.getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);

		matrices.pop();

		renderItemName(entity, matrices, vertexConsumers);
		matrices.pop();
	}

	protected void renderItemName(AbstractHologramBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
		var stack = entity.getHologramItem();
		var itemName = stack.isEmpty() || stack.isOf(Items.BARRIER) ? Text.literal("§cNo Item!") : stack.getName();
		var itemNameWidth = ctx.getTextRenderer().getWidth(itemName);

		var light = LightmapTextureManager.pack(15, 15);

		matrices.translate(0f, 1f, 0f);
		matrices.multiply(ctx.getRenderDispatcher().camera.getRotation());
		matrices.scale(-0.025f, -0.025f, 0.025f);

		ctx.getTextRenderer().draw(itemName, itemNameWidth / -2f, 0f, WHITE,false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, BACKGROUND, light);
	}
}
