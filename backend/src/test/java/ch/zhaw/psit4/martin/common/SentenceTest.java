package ch.zhaw.psit4.martin.common;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.api.types.Date;
import ch.zhaw.psit4.martin.api.types.Duration;
import ch.zhaw.psit4.martin.api.types.Location;
import ch.zhaw.psit4.martin.api.types.Money;
import ch.zhaw.psit4.martin.api.types.Ordinal;
import ch.zhaw.psit4.martin.api.types.Organization;
import ch.zhaw.psit4.martin.api.types.Percent;
import ch.zhaw.psit4.martin.api.types.Person;
import ch.zhaw.psit4.martin.api.types.Time;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class SentenceTest {

	@Autowired
	private StanfordCoreNLP stanfordNLP;

	@Test
	public void testPersonSimple() {
		Sentence sentence = new Sentence("Hello, my name is Chuck Norris.", stanfordNLP);

		Phrase phrase = sentence.popPhraseOfIMartinType(Person.class.getName());

		assertEquals(phrase.getType(), "PERSON");
		assertEquals(phrase.getValue(), "Chuck Norris");
	}

	@Test
	public void testPersonMultiple() {
		Sentence sentence = new Sentence("Today I met Donald Trump hanging out with Barack Obama and Putin. They were laughing alot.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfIMartinType(Person.class.getName());

		assertEquals(phrase1.getType(), "PERSON");
		assertEquals(phrase1.getValue(), "Donald Trump");
		
		
		Phrase phrase2 = sentence.popPhraseOfIMartinType(Person.class.getName());

		assertEquals(phrase2.getType(), "PERSON");
		assertEquals(phrase2.getValue(), "Barack Obama");
		
		Phrase phrase3 = sentence.popPhraseOfIMartinType(Person.class.getName());

		assertEquals(phrase3.getType(), "PERSON");
		assertEquals(phrase3.getValue(), "Putin");
	}
	
	@Test
	public void testDuration() {
		Sentence sentence = new Sentence("I was waiting for at least two weeks for my new iPhone.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfIMartinType(Duration.class.getName());

		assertEquals(phrase1.getType(), "DURATION");
		assertEquals(phrase1.getValue(), "at least two weeks");
		
	}
	
	@Test
	public void testMoney() {
		Sentence sentence = new Sentence("My car was on sale and I got a bounty of 100 dollar.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfIMartinType(Money.class.getName());

		assertEquals(phrase1.getType(), "MONEY");
		assertEquals(phrase1.getValue(), "100 dollar");
		
	}
	
	@Test
	public void testLocation() {
		Sentence sentence = new Sentence("Yesterday I wanted to fly to Dubai.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfIMartinType(Location.class.getName());

		assertEquals(phrase1.getType(), "LOCATION");
		assertEquals(phrase1.getValue(), "Dubai");
		
	}
	
	@Test
	public void testNumber() {
		Sentence sentence = new Sentence("Thank you thousand times!", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfIMartinType(Number.class.getName());

		assertEquals(phrase1.getType(), "NUMBER");
		assertEquals(phrase1.getValue(), "thousand");
		
	}
	
	@Test
	public void testOrdinal() {
		Sentence sentence = new Sentence("I will be the first in this race!", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfIMartinType(Ordinal.class.getName());

		assertEquals(phrase1.getType(), "ORDINAL");
		assertEquals(phrase1.getValue(), "first");
		
	}
	
	@Test
	public void testOrganization() {
		Sentence sentence = new Sentence("Facebook will take over the world if we don't stop them.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfIMartinType(Organization.class.getName());

		assertEquals(phrase1.getType(), "ORGANIZATION");
		assertEquals(phrase1.getValue(), "Facebook");
		
	}
	
	@Test
	public void testPercent() {
		Sentence sentence = new Sentence("If the glass is half empty, it is only 50 percent full.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfIMartinType(Percent.class.getName());

		assertEquals(phrase1.getType(), "PERCENT");
		assertEquals(phrase1.getValue(), "50 percent");
		
	}
	
	@Test
	public void testTime() {
		Sentence sentence = new Sentence("It was 12 o'clock when the New Year 's Eve rockets hit the sky.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfIMartinType(Time.class.getName());

		assertEquals(phrase1.getType(), "TIME");
		assertEquals(phrase1.getValue(), "12 o'clock");
		
		Phrase phrase2 = sentence.popPhraseOfIMartinType(Date.class.getName());
		
		assertEquals(phrase2.getType(), "DATE");
		assertEquals(phrase2.getValue(), "New Year 's Eve");
		
	}

}
