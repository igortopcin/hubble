package br.usp.ime.mig.hubble.xnat;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

/**
 * Deserializer for Java 8 temporal {@link LocalDateTime}s. Based on Jackson2's
 * {@link LocalDateTimeDeserializer}.
 */
public class XNATLocalDateTimeDeserializer extends StdScalarDeserializer<LocalDateTime> {
	private static final long serialVersionUID = 1L;

	private final static DateTimeFormatter DATETIME_FORMAT = new DateTimeFormatterBuilder()
			.appendPattern("yyyy-MM-dd HH:mm:ss")
			.parseLenient()
			.appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).toFormatter();

	public XNATLocalDateTimeDeserializer() {
		super(LocalDateTime.class);
	}

	@Override
	public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		switch (parser.getCurrentToken()) {
		case VALUE_STRING:
			String string = parser.getText().trim();
			if (string.length() == 0)
				return null;
			return LocalDateTime.parse(string, DATETIME_FORMAT);

		default:
			return LocalDateTimeDeserializer.INSTANCE.deserialize(parser, context);
		}
	}

	@Override
	public Object deserializeWithType(JsonParser parser, DeserializationContext context, TypeDeserializer deserializer)
			throws IOException {
		return deserializer.deserializeTypedFromAny(parser, context);
	}
}
