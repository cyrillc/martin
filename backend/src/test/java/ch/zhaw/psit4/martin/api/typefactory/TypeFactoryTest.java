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
import ch.zhaw.psit4.martin.api.types.MartinLocation;
import ch.zhaw.psit4.martin.api.types.MartinNumber;
import ch.zhaw.psit4.martin.api.types.MartinTimestamp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class TypeFactoryTest {

	@Test
	public void testNumber() {

		try {
			MartinNumber one = (MartinNumber) MartinTypeFactory.fromType(EMartinType.NUMBER, "1");
			assertEquals(one.getIntegerNumber().get(), (Integer) 1);
			assertEquals(one.getDoubleNumber().get(), (Double) 1.0);
			assertEquals(one.getRawFormat(), MartinNumber.RawFormat.NUMERIC);

			MartinNumber oneWord = (MartinNumber) MartinTypeFactory.fromType(EMartinType.NUMBER, "one");
			assertEquals(oneWord.getIntegerNumber().get(), (Integer) 1);
			assertEquals(oneWord.getDoubleNumber().get(), (Double) 1.0);
			assertEquals(oneWord.getRawFormat(), MartinNumber.RawFormat.WORD_EN);

			MartinNumber twoThousand = (MartinNumber) MartinTypeFactory.fromType(EMartinType.NUMBER, "two thousand");
			assertEquals(twoThousand.getIntegerNumber().get(), (Integer) 2000);
			assertEquals(twoThousand.getDoubleNumber().get(), (Double) 2000.0);
			assertEquals(twoThousand.getRawFormat(), MartinNumber.RawFormat.WORD_EN);

			MartinNumber eleven = (MartinNumber) MartinTypeFactory.fromType(EMartinType.NUMBER, "eleven");
			assertEquals(eleven.getIntegerNumber().get(), (Integer) 11);
			assertEquals(eleven.getDoubleNumber().get(), (Double) 11.0);
			assertEquals(eleven.getRawFormat(), MartinNumber.RawFormat.WORD_EN);

		} catch (Exception e) {
			fail("Expected no exception, but got: " + e.getMessage());
		}
	}

	@Test
	public void testTimestamp() {
		try {
			DateTimeZone timeZone = DateTimeZone.forID("Europe/Paris");

			// Today
			MartinTimestamp today = (MartinTimestamp) MartinTypeFactory.fromType(EMartinType.TIMESTAMP, "today");
			DateTime correctToday = DateTime.now(timeZone);

			assertEquals(today.getDatetime().get().getYear(), correctToday.getYear());
			assertEquals(today.getDatetime().get().getMonthOfYear(), correctToday.getMonthOfYear());
			assertEquals(today.getDatetime().get().getDayOfMonth(), correctToday.getDayOfMonth());

			// Yesterday
			MartinTimestamp yesterday = (MartinTimestamp) MartinTypeFactory.fromType(EMartinType.TIMESTAMP, "yesterday");
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
			MartinLocation zurich = (MartinLocation) MartinTypeFactory.fromType(EMartinType.LOCATION, "Zürich");

			assertEquals(zurich.getFormattedAddress().get(), "Zürich, Switzerland");
			assertEquals(zurich.getLatitude().get(), (Double) 47.3768866);
			assertEquals(zurich.getLongitude().get(), (Double) 8.541694);

			MartinLocation honolulu = (MartinLocation) MartinTypeFactory.fromType(EMartinType.LOCATION, "Honolulu");
			
			assertEquals(honolulu.getFormattedAddress().get(), "Honolulu, HI, USA");
			assertEquals(honolulu.getLatitude().get(), (Double)21.3069444);
			assertEquals(honolulu.getLongitude().get(), (Double)(-157.8583333));

		} catch (Exception e) {
			fail("Expected no exception, but got: " + e.getMessage());
		}

	}

}
