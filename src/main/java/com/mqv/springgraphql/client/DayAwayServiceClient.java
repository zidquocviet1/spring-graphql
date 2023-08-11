package com.mqv.springgraphql.client;

import com.apollographql.apollo3.api.ApolloResponse;
import com.apollographql.apollo3.api.Operation;
import com.apollographql.apollo3.api.Operations;
import com.apollographql.apollo3.api.Optional;
import com.apollographql.apollo3.api.json.BufferedSinkJsonWriter;
import com.daw.graphql.client.type.PartnerInventoryFilter;
import com.daw.graphql.clientoperation.PartnerInventoryListQuery;
import com.ptw.graphql.clientoperation.IdCardTypesQuery;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

@Component
public class DayAwayServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(DayAwayServiceClient.class);

    private final HttpClient httpClient;

    public DayAwayServiceClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
    }

    public CompletableFuture<List<IdCardTypesQuery.IdCardType>> getIdCardTypes(final String countryCode) {

        final com.ptw.graphql.clientoperation.IdCardTypesQuery query = new IdCardTypesQuery(countryCode);
        final HttpRequest request = buildRequest(query);

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(httpResponse -> {
                    final IdCardTypesQuery.Data data = assertSuccessAndExtractData(httpResponse, query);
                    return data.idCardTypes;
                });
    }

    public CompletableFuture<List<PartnerInventoryListQuery.OnPartner>> getPartnerInventoryList() {

        final PartnerInventoryListQuery query = new PartnerInventoryListQuery(Optional.present(new PartnerInventoryFilter(Optional.absent(), Optional.absent(),
                Optional.absent(), Optional.absent(), Optional.absent(), Optional.absent(), Optional.absent())));
        final HttpRequest request = buildRequest(query);

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(httpResponse ->
                {
                    // IntelliJ users: type parameters error “no instance of type variable exists so that Data conforms to Data”
                    // is not accurate; this might be fixed in Kotlin 1.8: https://youtrack.jetbrains.com/issue/KTIJ-21905/
                    final PartnerInventoryListQuery.Data data = assertSuccessAndExtractData(httpResponse, query);
                    return data.partnerInventoryList;
                })
                .thenApply(pageData -> {
                    if (pageData.data != null && !pageData.data.isEmpty()) {
                        return pageData.data.stream().map(data -> data.onPartner).collect(Collectors.toList());
                    }
                    return null;
                });
    }

    private <T extends Operation<U>, U extends Operation.Data> U assertSuccessAndExtractData(
            HttpResponse<String> httpResponse, T operation) {

        if (httpResponse.statusCode() != 200) {
            logger.warn("Received HTTP response status {} ({})", httpResponse.statusCode(),
                    httpResponse.headers().firstValue("paypal-debug-id").orElse("<debug id absent>"));
            throw new IllegalArgumentException();
        }

        ApolloResponse<U> response = Operations.parseJsonResponse(operation, httpResponse.body());

        if (response.hasErrors() || response.data == null) {
            //noinspection ConstantConditions
            response.errors.forEach(
                    error -> {
                        final Object legacyCode = java.util.Optional.ofNullable(error.getExtensions())
                                .map(extensions -> extensions.get("legacyCode"))
                                .orElse("<none>");
                        logger.warn("Received GraphQL error for {}: \"{}\" (legacyCode: {})",
                                response.operation.name(), error.getMessage(), legacyCode);
                    });

            throw new IllegalArgumentException();
        }

        return response.data;
    }

    private HttpRequest buildRequest(final Operation<?> operation) {

        final Buffer buffer = new Buffer();
        Operations.composeJsonRequest(operation, new BufferedSinkJsonWriter(buffer));

        return HttpRequest.newBuilder()
                .uri(URI.create("https://gateway-qa.dayaway.sg/platform/graphql"))
                .method("POST", HttpRequest.BodyPublishers.ofString(buffer.readUtf8()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Imh6MGhZajJHV19yT2JZeEFjUjNwWCJ9.eyJ1c2VyX3Blcm1pc3Npb24iOiJNUF9GT1VOREVSX0tFWV9BQ0NFU1MsTVBfTUVNQkVSX09OTFlfQUNDRVNTIiwidXNlcl9yZWFsbSI6Ik1BUktFVFBMQUNFIiwicGFydG5lcl9yb2xlIjoiIiwiZGFfdXNlcl9pZCI6IjNlZmVmYTgwLWI3MDgtNGUyYi04ODE3LWY0NmY2YjlhZDQxNiIsImlzcyI6Imh0dHBzOi8vZGF5YXdheS1xYS5ldS5hdXRoMC5jb20vIiwic3ViIjoiYXV0aDB8NjI4MjJiOTdiMDk5NzAwMDY5OTA0NjEzIiwiYXVkIjpbImh0dHBzOi8vcGxhdGZvcm0uYXBpLyIsImh0dHBzOi8vZGF5YXdheS1xYS5ldS5hdXRoMC5jb20vdXNlcmluZm8iXSwiaWF0IjoxNjkwOTg5NzI5LCJleHAiOjE2OTEwNzYxMjEsImF6cCI6IllpMVhKN3kzcUwxRFlucFBKUGZwdXpUdmwybVZ1UUVkIiwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsInBlcm1pc3Npb25zIjpbIkhPTUVfVklFVyJdfQ.LjsgZIQr1O6hWNRwKhBtjIiUqlcab7jH9WlkAOu7XdkybO2TW5kgio6osufd0-rU8eVR_pRRP9m6Nv6ugoIJVMGJvhixIevuHgRqHKwPU8TOYxs4kSug1wmKKsYDkKlWaJGuwUXSnrY4nIZ9lR0FaDlQiyTCNhz3dNkBVNIsj9LgX2ufbG0jZE4YL_k5ZCSYlB-HzNOr0qmYVnQRwzE1pqZgtXdw26s61z4kt5xks6Qb5uhlT1ox8iLUjgd2M908i_atl_E07qhx7J_neS8JB8ngZhyMEDJK6zJAwbrnnYZJXR9EvlaSHPFZ4NxjP-iBlBvc2cekDxf1G9Kwg0pTbw")
//                .header("Braintree-Version", BRAINTREE_VERSION)
                .build();
    }
}
