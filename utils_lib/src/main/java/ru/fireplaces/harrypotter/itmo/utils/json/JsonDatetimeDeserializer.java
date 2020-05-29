package ru.fireplaces.harrypotter.itmo.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.fireplaces.harrypotter.itmo.utils.exception.BadDateFormatException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Custom JSON date and time deserializer.
 *
 * @author seniorkot
 */
public class JsonDatetimeDeserializer extends JsonDeserializer<Date> {

    /**
     * List of date patterns.
     */
    private final List<String> datetimeFormats =
            Arrays.asList("d.M.yyyy HH:mm", "d/M/yyyy HH:mm", "d-M-yyyy HH:mm",
                    "yyyy.M.d HH:mm", "yyyy/M/d HH:mm", "yyyy-M-d HH:mm");

    /**
     * Deserialization method.
     *
     * @param jsonParser JSON parser
     * @param deserializationContext Deserialization context
     *
     * @return Parsed {@link Date}
     * @throws IOException IO Exception
     * @throws JsonProcessingException JSON Processing Exception
     * @throws BadDateFormatException When no pattern matches
     */
    @Override
    public Date deserialize(JsonParser jsonParser,
                            DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        for (String format : datetimeFormats) {
            try {
                return new SimpleDateFormat(format).parse(jsonParser.getText());
            } catch (ParseException ignored) { }
        }
        throw new BadDateFormatException(jsonParser.getText(), datetimeFormats);
    }
}
