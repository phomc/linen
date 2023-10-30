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
package dev.phomc.linen.api.scheduler;

import java.util.concurrent.ScheduledExecutorService;

import net.minecraft.server.MinecraftServer;

import dev.phomc.linen.impl.scheduler.AsExecutorService;

/**
 * A {@link ScheduledExecutorService} that is associated with a server.
 */
public interface ServerExecutorService extends ScheduledExecutorService {
	static ServerExecutorService get(MinecraftServer server) {
		return (ServerExecutorService) ((AsExecutorService) server).linen$asExecutorService();
	}
}
