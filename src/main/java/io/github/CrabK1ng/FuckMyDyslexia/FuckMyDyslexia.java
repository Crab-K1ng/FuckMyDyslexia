package io.github.CrabK1ng.FuckMyDyslexia;

import dev.crmodders.cosmicquilt.api.entrypoint.ModInitializer;
import org.quiltmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;

public class FuckMyDyslexia implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("FuckMyDyslexia");

	public static boolean reload = true;
	public static boolean isCommand = false;
	public static ArrayList<String> suggestions = new ArrayList<String>();
	public static ArrayList<String> commands = new ArrayList<String>();

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("FuckMyDyslexia Initialized!");
	}

	@Unique
	public static void MakeSuggestions(String inputText){
		if (inputText.startsWith("/")){
			isCommand = true;
			for (String each : commands) {
				if (each.startsWith(inputText.replaceFirst("/",""))) {
					suggestions.add(each);
				}
			}
		} else{
			isCommand = false;
		}
	}

	@Unique
	public static void clearCommands(){
		commands.clear();
		commands.add("reload");
	}
}

