
package io.kestra.core.serializers;

import com.amazon.ion.IonSystem;
import com.amazon.ion.system.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.ion.IonObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.diff.JsonDiff;
import io.kestra.core.plugins.PluginModule;
import io.kestra.core.runners.RunContextModule;
import io.kestra.core.serializers.ion.IonFactory;
import io.kestra.core.serializers.ion.IonModule;
import org.apache.commons.lang3.tuple.Pair;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.IOException;
import java.time.Duration;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public final class JacksonMapper {
    public static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {};
    public static final TypeReference<List<Object>> LIST_TYPE_REFERENCE = new TypeReference<>() {};
    public static final TypeReference<Object> OBJECT_TYPE_REFERENCE = new TypeReference<>() {};

    private JacksonMapper() {}

    private static final ObjectMapper MAPPER = JacksonMapper.configure(
        new ObjectMapper()
    );

    private static final ObjectMapper NON_STRICT_MAPPER = MAPPER
        .copy()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static ObjectMapper ofJson() {
        return MAPPER;
    }

    public static ObjectMapper ofJson(boolean strict) {
        return strict ? MAPPER : NON_STRICT_MAPPER;
    }

    private static final ObjectMapper YAML_MAPPER = JacksonMapper.configure(
        new ObjectMapper(
            YAMLFactory
                .builder()
                .loaderOptions(new LoaderOptions())
                .configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true)
                .configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false)
                .configure(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID, false)
                .configure(YAMLGenerator.Feature.SPLIT_LINES, false)
                .build()
        )
    );

    public static ObjectMapper ofYaml() {
        return YAML_MAPPER;
    }

    public static Map<String, Object> toMap(Object object, ZoneId zoneId) {
        return MAPPER
            .copy()
            .setTimeZone(TimeZone.getTimeZone(zoneId.getId()))
            .convertValue(object, MAP_TYPE_REFERENCE);
    }

    public static Map<String, Object> toMap(Object object) {
        return MAPPER.convertValue(object, MAP_TYPE_REFERENCE);
    }

    public static <T> T toMap(Object map, Class<T> cls) {
        return MAPPER.convertValue(map, cls);
    }

    public static Map<String, Object> toMap(String json) throws JsonProcessingException {
        return MAPPER.readValue(json, MAP_TYPE_REFERENCE);
    }

    public static List<Object> toList(String json) throws JsonProcessingException {
        return MAPPER.readValue(json, LIST_TYPE_REFERENCE);
    }
    public static Object toObject(String json) throws JsonProcessingException {
        return MAPPER.readValue(json, OBJECT_TYPE_REFERENCE);
    }

    public static <T> T cast(Object object, Class<T> cls) throws JsonProcessingException {
        return MAPPER.readValue(MAPPER.writeValueAsString(object), cls);
    }

    public static <T> String log(T Object) {
        try {
            return YAML_MAPPER.writeValueAsString(Object);
        } catch (JsonProcessingException ignored) {
            return "Failed to log " + Object.getClass();
        }
    }

    private static final ObjectMapper ION_MAPPER = createIonObjectMapper();

    public static ObjectMapper ofIon() {
        return ION_MAPPER;
    }

    private static ObjectMapper configure(ObjectMapper mapper) {
        SimpleModule durationDeserialization = new SimpleModule();
        durationDeserialization.addDeserializer(Duration.class, new DurationDeserializer());

        return mapper
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .registerModule(new ParameterNamesModule())
            .registerModules(new GuavaModule())
            .registerModule(new PluginModule())
            .registerModule(new RunContextModule())
            .registerModule(durationDeserialization)
            .setTimeZone(TimeZone.getDefault());
    }

    private static ObjectMapper createIonObjectMapper() {
        return configure(new IonObjectMapper(new IonFactory(createIonSystem())))
            .setSerializationInclusion(JsonInclude.Include.ALWAYS)
            .registerModule(new IonModule());
    }

    private static IonSystem createIonSystem() {
         return IonSystemBuilder.standard()
            .withIonTextWriterBuilder(IonTextWriterBuilder.standard().withWriteTopLevelValuesOnNewLines(true))
            .build();
    }

    public static Pair<JsonNode, JsonNode> getBiDirectionalDiffs(Object previous, Object current)  {
        JsonNode previousJson = MAPPER.valueToTree(previous);
        JsonNode newJson = MAPPER.valueToTree(current);

        JsonNode patchPrevToNew = JsonDiff.asJson(previousJson, newJson);
        JsonNode patchNewToPrev = JsonDiff.asJson(newJson, previousJson);

        return Pair.of(patchPrevToNew, patchNewToPrev);
    }

    public static String applyPatches(Object object, List<JsonNode> patches) throws JsonProcessingException {
        for (JsonNode patch : patches) {
            try {
                // Required for ES
                if (patch.findValue("value") == null) {
                    ((ObjectNode) patch.get(0)).set("value", (JsonNode) null);
                }
                JsonNode current = MAPPER.valueToTree(object);
                object = JsonPatch.fromJson(patch).apply(current);
            } catch (IOException | JsonPatchException e) {
                throw new RuntimeException(e);
            }
        }
        return MAPPER.writeValueAsString(object);
    }


}
