package ch.zhaw.psit4.martin.language.typefactory;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.EBaseType;
import ch.zhaw.psit4.martin.api.types.MLocation;
import ch.zhaw.psit4.martin.api.types.MNumber;
import ch.zhaw.psit4.martin.api.types.MTimestamp;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import ch.zhaw.psit4.martin.language.typefactory.BaseTypeFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class TypeFactoryTest {

	@Test
	public void testNumber() {

		try {
			Phrase phrase = new Phrase("1");
			phrase.setType(EBaseType.NUMBER);
			MNumber one = (MNumber) BaseTypeFactory.fromPhrase(phrase, new AnnotatedSentence());
			assertEquals(one.getIntegerNumber().get(), (Integer) 1);
			assertEquals(one.getDoubleNumber().get(), (Double) 1.0);
			assertEquals(one.getRawFormat(), MNumber.RawFormat.NUMERIC);

			phrase = new Phrase("one");
			phrase.setType(EBaseType.NUMBER);
			MNumber oneWord = (MNumber) BaseTypeFactory.fromPhrase(phrase, new AnnotatedSentence());
			assertEquals(oneWord.getIntegerNumber().get(), (Integer) 1);
			assertEquals(oneWord.getDoubleNumber().get(), (Double) 1.0);
			assertEquals(oneWord.getRawFormat(), MNumber.RawFormat.WORD_EN);

			phrase = new Phrase("two thousand");
			phrase.setType(EBaseType.NUMBER);
			MNumber twoThousand = (MNumber) BaseTypeFactory.fromPhrase(phrase, new AnnotatedSentence());
			assertEquals(twoThousand.getIntegerNumber().get(), (Integer) 2000);
			assertEquals(twoThousand.getDoubleNumber().get(), (Double) 2000.0);
			assertEquals(twoThousand.getRawFormat(), MNumber.RawFormat.WORD_EN);

			phrase = new Phrase("eleven");
			phrase.setType(EBaseType.NUMBER);
			MNumber eleven = (MNumber) BaseTypeFactory.fromPhrase(phrase, new AnnotatedSentence());
			assertEquals(eleven.getIntegerNumber().get(), (Integer) 11);
			assertEquals(eleven.getDoubleNumber().get(), (Double) 11.0);
			assertEquals(eleven.getRawFormat(), MNumber.RawFormat.WORD_EN);

		} catch (Exception e) {
			fail("Expected no exception, but got: " + e.getMessage());
		}
	}

	@Test
	public void testTimestamp() {
		try {
			DateTimeZone timeZone = DateTimeZone.forID("Europe/Paris");

			// Today
			Phrase phrase = new Phrase("today");
			phrase.setType(EBaseType.TIMESTAMP);
			MTimestamp today = (MTimestamp) BaseTypeFactory.fromPhrase(phrase, new AnnotatedSentence());
			DateTime correctToday = DateTime.now(timeZone);

			assertEquals(today.getDatetime().get().getYear(), correctToday.getYear());
			assertEquals(today.getDatetime().get().getMonthOfYear(), correctToday.getMonthOfYear());
			assertEquals(today.getDatetime().get().getDayOfMonth(), correctToday.getDayOfMonth());

			// Yesterday
			phrase = new Phrase("yesterday");
			phrase.setType(EBaseType.TIMESTAMP);
			
			MTimestamp yesterday = (MTimestamp) BaseTypeFactory.fromPhrase(phrase, new AnnotatedSentence());
			DateTime correctYesterday = DateTime.now(timeZone).minusDays(1);

			assertEquals(yesterday.getDatetime().get().getYear(), correctYesterday.getYear());
			assertEquals(yesterday.getDatetime().get().getMonthOfYear(), correctYesterday.getMonthOfYear());
			assertEquals(yesterday.getDatetime().get().getDayOfMonth(), correctYesterday.getDayOfMonth());
		} catch (Exception e) {
			fail("Expected no exception, but got: " + e.getMessage());
		}
	}

	@Test
	public void testLocation() {
		// Skip test if internet connection is not available
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress("google.com", 80), 100);
		} catch (IOException e) {
			return;
		}

		try {
			Phrase phrase = new Phrase("Zürich");
			phrase.setType(EBaseType.LOCATION);
			MLocation zurich = (MLocation) BaseTypeFactory.fromPhrase(phrase, new AnnotatedSentence());

			assertEquals(zurich.getFormattedAddress().get(), "Zürich, Switzerland");
			assertEquals(zurich.getLatitude().get(), (Double) 47.3768866);
			assertEquals(zurich.getLongitude().get(), (Double) 8.541694);

			
			phrase = new Phrase("Honolulu");
			phrase.setType(EBaseType.LOCATION);
			MLocation honolulu = (MLocation) BaseTypeFactory.fromPhrase(phrase, new AnnotatedSentence());
			
			assertEquals(honolulu.getFormattedAddress().get(), "Honolulu, HI, USA");
			assertEquals(honolulu.getLatitude().get(), (Double)21.3069444);
			assertEquals(honolulu.getLongitude().get(), (Double)(-157.8583333));

		} catch (Exception e) {
			fail("Expected no exception, but got: " + e.getMessage());
		}

	}

}
