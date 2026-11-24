/*
 * SPDX-License-Identifier: AGPL-3.0-only
 * Copyright © 2026 Cubicake.
 * This file is part of RaycastedAntiESP.
 * RaycastedAntiESP is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License v3.0 only, which can be accessed at https://www.gnu.org/licenses/agpl-3.0.html.
 * See README.md for warranty disclaimer and further information.
 */

package games.cubi.raycastedantiesp.core.config.raycast;

import games.cubi.raycastedantiesp.core.config.ConfigReader;
import org.spongepowered.configurate.ConfigurationNode;

public class PlayerConfig extends RaycastConfig {
    /** The distance in blocks the game draws a player's name tag within. */
    private static final int NAME_TAG_DISTANCE = 64;

    private final boolean onlyCheckSneaking;
    private final double onlyCheckSneakingRadiusSquared;

    private PlayerConfig(RaycastConfig config, boolean onlyCheckSneaking, int onlyCheckSneakingRadius) {
        super(config.enabled(), config.hideSoundsWhenHidden(), config.getMaxOccludingCount(), config.getAlwaysShowRadius(),
                config.getRaycastRadius(), config.hideOnSpawnDistance(), config.getVisibleRecheckIntervalTicks(),
                config.keepClientEntityWhenHidden());
        this.onlyCheckSneaking = onlyCheckSneaking;
        this.onlyCheckSneakingRadiusSquared = (double) onlyCheckSneakingRadius * onlyCheckSneakingRadius;
    }

    public static PlayerConfig load(ConfigurationNode node, String path) {
        return new PlayerConfig(
                RaycastConfig.load(node, path, true, true),
                ConfigReader.bool(ConfigReader.node(node, "only-check-sneaking"), path + ".only-check-sneaking"),
                node.node("only-check-sneaking-radius").getInt(NAME_TAG_DISTANCE)
        );
    }

    public boolean onlyCheckSneaking() {
        return onlyCheckSneaking;
    }

    /** Whether a player who is not sneaking is always shown at this distance. */
    public boolean alwaysShowStanding(double distanceSquared) {
        return onlyCheckSneaking && distanceSquared <= onlyCheckSneakingRadiusSquared;
    }
}
