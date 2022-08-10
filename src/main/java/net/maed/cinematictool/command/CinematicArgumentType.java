package net.maed.cinematictool.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.maed.cinematictool.cinematic.Cinematic;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class CinematicArgumentType implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("test", "cinematic");

    public static CinematicArgumentType cinematic() {
        return new CinematicArgumentType();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Cinematic.CINEMATIC_LIST.forEach((name, cinematic) -> {
            builder.suggest(name);
        });
        return builder.buildFuture();
    }

    @Override
    public String parse(StringReader reader) {
        int argBeginning = reader.getCursor();
        if (!reader.canRead()) {
            reader.skip();
        }
        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }
        String argument = reader.getString().substring(argBeginning, reader.getCursor());
        return argument;
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
