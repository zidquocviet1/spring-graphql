package com.mqv.springgraphql.adapter;

import com.apollographql.apollo3.api.Adapter;
import com.apollographql.apollo3.api.CustomScalarAdapters;
import com.apollographql.apollo3.api.json.JsonReader;
import com.apollographql.apollo3.api.json.JsonWriter;
import io.micrometer.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

public class BigDecimalAdapter implements Adapter<BigDecimal> {
    @Override
    public BigDecimal fromJson(@NotNull JsonReader jsonReader, @NotNull CustomScalarAdapters customScalarAdapters) throws IOException {
        return StringUtils.isBlank(jsonReader.nextString()) ? null : new BigDecimal(Objects.requireNonNull(jsonReader.nextString()));
    }

    @Override
    public void toJson(@NotNull JsonWriter jsonWriter, @NotNull CustomScalarAdapters customScalarAdapters, BigDecimal bigDecimal) throws IOException {
        jsonWriter.value(jsonWriter.toString());
    }
}
