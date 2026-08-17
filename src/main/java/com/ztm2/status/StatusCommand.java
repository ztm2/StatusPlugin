package com.ztm2.status;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class StatusCommand {
    public static final LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("status")
            .then(Commands.argument("status", StringArgumentType.string())
                    .requires(s->s.getExecutor() instanceof Player)
                    .executes(StatusCommand::set))
            .then(Commands.literal("clear")
                    .requires(s->s.getExecutor() instanceof Player)
                    .executes(StatusCommand::clear));

    public static int set(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getExecutor();
        String status = ctx.getArgument("status", String.class);
        if(player == null || status == null) return 0;
        Utils.setStatus(player,status);
        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>ステータスメッセージを更新しました</green>"));
        return 1;
    }

    public static int clear(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getExecutor();
        if(player == null) return 0;
        Utils.clearStatus(player);
        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>ステータスメッセージをクリアしました</green>"));
        return 1;
    }
}
