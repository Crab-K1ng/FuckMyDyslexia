package io.github.CrabK1ng.FuckMyDyslexia.mixins;

import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.networking.client.ChatSender;
import io.github.CrabK1ng.FuckMyDyslexia.FuckMyDyslexia;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatSender.class)
public class ChatSenderMixin {

    @Inject(method = "sendMessageOrCommand", at = @At("HEAD"),  cancellable = true)
    private static void sendMessageOrCommand(Chat chat, Account account, String messageText, CallbackInfo ci){
        if (messageText != null && messageText.length() != 0) {
            if (messageText.equalsIgnoreCase("/reload") || messageText.equalsIgnoreCase("/reload ")) {
                FuckMyDyslexia.reload = true;
                FuckMyDyslexia.clearCommands();
                ci.cancel();
            }
        }
    }

}
