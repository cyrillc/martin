package ch.zhaw.psit4.martin.api.typefactory;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.api.types.EMartinType;
import ch.zhaw.psit4.martin.api.types.Number;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class TypeFactoryTest {

	@Test
	public void testNumber() {
		
		try {
			Number one = (Number) MartinTypeFactory.fromType(EMartinType.NUMBER, "1");
			assertEquals(one.getIntegerNumber().get(), (Integer) 1);
			assertEquals(one.getDoubleNumber().get(), (Double) 1.0);
			assertEquals(one.getRawFormat(), "numeric");
			
			Number oneWord = (Number) MartinTypeFactory.fromType(EMartinType.NUMBER, "one");
			assertEquals(oneWord.getIntegerNumber().get(), (Integer) 1);
			assertEquals(oneWord.getDoubleNumber().get(), (Double) 1.0);
			assertEquals(oneWord.getRawFormat(), "words-en");

			Number twoThousand = (Number) MartinTypeFactory.fromType(EMartinType.NUMBER, "two thousand");
			assertEquals(twoThousand.getIntegerNumber().get(), (Integer) 2000);
			assertEquals(twoThousand.getDoubleNumber().get(), (Double) 2000.0);
			assertEquals(twoThousand.getRawFormat(), "words-en");

			Number eleven = (Number) MartinTypeFactory.fromType(EMartinType.NUMBER, "eleven");
			assertEquals(eleven.getIntegerNumber().get(), (Integer) 11);
			assertEquals(eleven.getDoubleNumber().get(), (Double) 11.0);
			assertEquals(eleven.getRawFormat(), "words-en");
		
		} catch(Exception e){
			 fail("Expected no exception, but got: " + e.getMessage());
		}
	}

}
