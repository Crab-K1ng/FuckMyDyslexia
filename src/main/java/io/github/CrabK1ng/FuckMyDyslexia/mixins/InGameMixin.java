package io.github.CrabK1ng.FuckMyDyslexia.mixins;

import finalforeach.cosmicreach.ClientSingletons;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.chat.ChatMessage;
import finalforeach.cosmicreach.gamestates.*;
import finalforeach.cosmicreach.networking.client.ChatSender;
import io.github.CrabK1ng.FuckMyDyslexia.FuckMyDyslexia;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(InGame.class)
public class InGameMixin {

    @Inject(method = "onSwitchTo",at = @At("TAIL"))
    public void onSwitchTo(CallbackInfo ci) {
        if (FuckMyDyslexia.reload){
            FuckMyDyslexia.clearCommands();
            Chat chat = Chat.MAIN_CLIENT_CHAT;
            String inputText = "/?";
            ChatSender.sendMessageOrCommand(chat, ClientSingletons.ACCOUNT, inputText);

            ChatMessage message = chat.getLastMessage(0);
            String text = message.messageText();
            String sender = message.getSenderName();

            if (sender == null) {
                Pattern pattern = Pattern.compile("/(\\w+)");
                Matcher matcher = pattern.matcher(text);
                while (matcher.find()) {
                    FuckMyDyslexia.commands.add(matcher.group(1));
                }
            }
        }
    }

    @Inject(method = "switchAwayTo",at = @At("TAIL"))
    public void switchAwayTo(GameState gameState, CallbackInfo ci) {
        if ((gameState instanceof PauseMenu) || (gameState instanceof LoadingGame) || (gameState instanceof YouDiedMenu) || (gameState instanceof ChatMenu)){
            FuckMyDyslexia.reload = false;
        } else {
            FuckMyDyslexia.reload = true;
            FuckMyDyslexia.clearCommands();
        }
    }
}