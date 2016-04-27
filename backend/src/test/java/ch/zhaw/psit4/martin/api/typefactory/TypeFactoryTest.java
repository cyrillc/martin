package ch.zhaw.psit4.martin.api.typefactory;

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

import ch.zhaw.psit4.martin.api.types.EMartinType;
import ch.zhaw.psit4.martin.api.types.Location;
import ch.zhaw.psit4.martin.api.types.Number;
import ch.zhaw.psit4.martin.api.types.Timestamp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class TypeFactoryTest {

	@Test
	public void testNumber() {

		try {
			Number one = (Number) MartinTypeFactory.fromType(EMartinType.NUMBER, "1");
			assertEquals(one.getIntegerNumber().get(), (Integer) 1);
			assertEquals(one.getDoubleNumber().get(), (Double) 1.0);
			assertEquals(one.getRawFormat(), Number.RawFormat.NUMERIC);

			Number oneWord = (Number) MartinTypeFactory.fromType(EMartinType.NUMBER, "one");
			assertEquals(oneWord.getIntegerNumber().get(), (Integer) 1);
			assertEquals(oneWord.getDoubleNumber().get(), (Double) 1.0);
			assertEquals(oneWord.getRawFormat(), Number.RawFormat.WORD_EN);

			Number twoThousand = (Number) MartinTypeFactory.fromType(EMartinType.NUMBER, "two thousand");
			assertEquals(twoThousand.getIntegerNumber().get(), (Integer) 2000);
			assertEquals(twoThousand.getDoubleNumber().get(), (Double) 2000.0);
			assertEquals(twoThousand.getRawFormat(), Number.RawFormat.WORD_EN);

			Number eleven = (Number) MartinTypeFactory.fromType(EMartinType.NUMBER, "eleven");
			assertEquals(eleven.getIntegerNumber().get(), (Integer) 11);
			assertEquals(eleven.getDoubleNumber().get(), (Double) 11.0);
			assertEquals(eleven.getRawFormat(), Number.RawFormat.WORD_EN);

		} catch (Exception e) {
			fail("Expected no exception, but got: " + e.getMessage());
		}
	}

	@Test
	public void testTimestamp() {
		try {
			DateTimeZone timeZone = DateTimeZone.forID("Europe/Paris");

			// Today
			Timestamp today = (Timestamp) MartinTypeFactory.fromType(EMartinType.TIMESTAMP, "today");
			DateTime correctToday = DateTime.now(timeZone);

			assertEquals(today.getDatetime().get().getYear(), correctToday.getYear());
			assertEquals(today.getDatetime().get().getMonthOfYear(), correctToday.getMonthOfYear());
			assertEquals(today.getDatetime().get().getDayOfMonth(), correctToday.getDayOfMonth());

			// Yesterday
			Timestamp yesterday = (Timestamp) MartinTypeFactory.fromType(EMartinType.TIMESTAMP, "yesterday");
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
			Location zurich = (Location) MartinTypeFactory.fromType(EMartinType.LOCATION, "Zürich");

			assertEquals(zurich.getFormattedAddress().get(), "Zürich, Switzerland");
			assertEquals(zurich.getLatitude().get(), (Double) 47.3768866);
			assertEquals(zurich.getLongitude().get(), (Double) 8.541694);

			Location honolulu = (Location) MartinTypeFactory.fromType(EMartinType.LOCATION, "Honolulu");
			
			assertEquals(honolulu.getFormattedAddress().get(), "Honolulu, HI, USA");
			assertEquals(honolulu.getLatitude().get(), (Double)21.3069444);
			assertEquals(honolulu.getLongitude().get(), (Double)(-157.8583333));

		} catch (Exception e) {
			fail("Expected no exception, but got: " + e.getMessage());
		}

	}

}
