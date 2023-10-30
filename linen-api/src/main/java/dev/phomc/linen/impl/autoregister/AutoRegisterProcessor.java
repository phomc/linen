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
package dev.phomc.linen.impl.autoregister;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import dev.phomc.linen.api.autoregister.AutoRegister;
import dev.phomc.linen.api.autoregister.BlockItemProvider;

public final class AutoRegisterProcessor {
	public static void register(Class<?> clazz) {
		var annotatedClass = clazz.getAnnotation(AutoRegister.class);

		Objects.requireNonNull(annotatedClass, "Class must be annotated with @AutoRegister");

		Arrays.stream(clazz.getDeclaredFields())
				.filter(AutoRegisterProcessor::filterFields)
				.forEach(field -> {
					var name = field.getName().toLowerCase();
					var annotatedField = field.getAnnotation(AutoRegister.class);
					if (annotatedField != null && !annotatedField.value().isEmpty()) name = annotatedField.value();

					Object value;

					try {
						value = field.get(null);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}

					_register(annotatedClass.value() + ":" + name, value);
				});
	}

	private static void _register(String id, Object object) {
		if (object instanceof Item item) {
			_register(Registries.ITEM, id, item);
		} else if (object instanceof Block block) {
			_register(Registries.BLOCK, id, block);

			if (block instanceof BlockItemProvider blockItem && blockItem.createBlockItem() != null) {
				_register(Registries.ITEM, id, blockItem.createBlockItem());
			}
		} else if (object instanceof BlockEntityType<?> blockEntityType) {
			_register(Registries.BLOCK_ENTITY_TYPE, id, blockEntityType);
		} else if (object instanceof EntityType<?> entityType) {
			_register(Registries.ENTITY_TYPE, id, entityType);
		}
	}

	private static <T> void _register(Registry<T> registry, String id, T obj) {
		Registry.register(registry, id, obj);
	}

	private static boolean filterFields(Field field) {
		if (!Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) return false;

		var annotatedField = field.getAnnotation(AutoRegister.class);

		if (annotatedField == null) {
			return true;
		} else {
			return !annotatedField.skip();
		}
	}
}
