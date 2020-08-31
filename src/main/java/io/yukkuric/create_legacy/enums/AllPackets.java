package io.yukkuric.create_legacy.enums;

import io.yukkuric.create_legacy.CreateLegacy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public enum AllPackets {
    NULL;

    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(CreateLegacy.ID, "network");
    public static final String NETWORK_VERSION = new ResourceLocation(CreateLegacy.ID, "1").toString();
    public static SimpleChannel channel;

    public static void registerPackets() {
        channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME).serverAcceptedVersions(s -> true)
                .clientAcceptedVersions(s -> true).networkProtocolVersion(() -> NETWORK_VERSION).simpleChannel();
//        for (AllPackets packet : values())
//            packet.packet.register();
    }
}
