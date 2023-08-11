package com.mqv.springgraphql.adapter;

import com.apollographql.apollo3.api.Adapter;
import com.apollographql.apollo3.api.CustomScalarAdapters;
import com.apollographql.apollo3.api.json.JsonReader;
import com.apollographql.apollo3.api.json.JsonWriter;
import io.micrometer.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class UUIDAdapter implements Adapter<UUID> {
    @Override
    public UUID fromJson(@NotNull JsonReader jsonReader, @NotNull CustomScalarAdapters customScalarAdapters) throws IOException {
        return StringUtils.isBlank(jsonReader.nextString()) ? null : UUID.fromString(Objects.requireNonNull(jsonReader.nextString()));
    }

    @Override
    public void toJson(@NotNull JsonWriter jsonWriter, @NotNull CustomScalarAdapters customScalarAdapters, UUID uuid) throws IOException {
        jsonWriter.value(uuid.toString());
    }
}
