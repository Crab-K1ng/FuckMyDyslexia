package io.github.CrabK1ng.FuckMyDyslexia.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import finalforeach.cosmicreach.ClientSingletons;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.chat.ChatMessage;
import finalforeach.cosmicreach.gamestates.ChatMenu;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.networking.client.ChatSender;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import io.github.CrabK1ng.FuckMyDyslexia.FuckMyDyslexia;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static finalforeach.cosmicreach.gamestates.ChatMenu.minY;
import static io.github.CrabK1ng.FuckMyDyslexia.FuckMyDyslexia.*;

@Mixin(ChatMenu.class)
public class ChatMenuMixin extends GameState {

    @Shadow
    String inputText;

    @Shadow
    int desiredCharIdx;

    @Unique
    int selected = 0;

    @Inject(method = "keyTyped",at = @At("TAIL"))
    public void keyTyped(char character, CallbackInfoReturnable<Boolean> cir){
        suggestions.clear();
        MakeSuggestions(inputText);
        if (!(character == '\t')){
            selected = 0;
        }
    }

    @Inject(method = "keyTyped",at = @At(value = "RETURN", shift = At.Shift.BEFORE))
    public void keyDelete(char character, CallbackInfoReturnable<Boolean> cir){
        suggestions.clear();
        MakeSuggestions(inputText);
        if (!(character == '\t')){
            selected = 0;
        }
    }

    @Inject(method = "updateRepeatMessageIdx", at = @At("HEAD"), cancellable = true)
    public void updateRepeatMessageIdxHead(int offset, CallbackInfo ci){
        if (offset == -1){ // down
            if (selected >= 0){
                selected += 1;
                ci.cancel();
            } else {
                selected += 1;
            }
        }

        if (offset == 1){ // up
            if (selected > 0){
                selected -= 1;
                ci.cancel();
            } else {
                selected -= 1;
            }
        }
    }

    @Inject(method = "updateRepeatMessageIdx", at = @At("TAIL"))
    public void updateRepeatMessageIdxTail(int offset, CallbackInfo ci){
        suggestions.clear();
        MakeSuggestions(inputText);
    }

    @Unique
    public boolean isSelected(int suggestionsIdx){
        if (selected == suggestionsIdx){
            return true;
        }
        return false;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/graphics/g2d/SpriteBatch;end()V", shift = At.Shift.BEFORE))
    private void render(CallbackInfo ci) {
        if (isCommand){
            Iterator<String> iterator = suggestions.iterator();

            long msec = System.currentTimeMillis();


            float y = minY - -6.0F ;
            float x = 0;

            Vector2 tmpVec = new Vector2();

            FontRenderer.getTextDimensions(this.uiViewport, inputText, tmpVec);
            x += tmpVec.x;
            x += 20.0F;


            int suggestionsIdx = 0;
            while (iterator.hasNext()) {

                String textToRender = iterator.next();

                batch.setColor(1.0F, 1.0F, 1.0F, 1.0F);

                if (msec % 1500L > 750L) {
                    if (isSelected(suggestionsIdx)){
                        batch.setColor(Color.YELLOW);
                    }
                }

                FontRenderer.drawText(batch, this.uiViewport, textToRender, x, y, HorizontalAnchor.LEFT_ALIGNED, VerticalAnchor.BOTTOM_ALIGNED);

                suggestionsIdx += 1;
                y += 20.0F;
            }

            if (Gdx.input.isKeyJustPressed(61)) {
                if (!suggestions.isEmpty() && selected >= 0 && selected < suggestions.size()) {
                    inputText = "/";
                    inputText = inputText.concat(suggestions.get(selected) + " ");
                    desiredCharIdx = inputText.length();
                    suggestions.clear();
                }
            }
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/gamestates/ChatMenu;switchToGameState(Lfinalforeach/cosmicreach/gamestates/GameState;)V", shift = At.Shift.AFTER))
    private void renderSwitchToGameState(CallbackInfo ci) {
        suggestions.clear();
        isCommand = false;
    }

    @Override
    public void onSwitchTo() {
        if (FuckMyDyslexia.openWithSlash){
            FuckMyDyslexia.openWithSlash = false;
            inputText = "/";
            desiredCharIdx += 1;
            suggestions.clear();
            MakeSuggestions(inputText);
        }
    }
}
