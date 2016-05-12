package ch.zhaw.psit4.martin.common;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.api.types.EBaseType;
import edu.stanford.nlp.pipeline.StanfordCoreNLPClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class SentenceTest {
	@Autowired
	private StanfordCoreNLPClient stanfordNLP;

	@Test
	public void testPersonSimple() {
		Sentence sentence = new Sentence("Hello, my name is Chuck Norris.", stanfordNLP);

		Phrase phrase = sentence.popPhraseOfType(EBaseType.PERSON);

		assertEquals(phrase.getType(), EBaseType.PERSON);
		assertEquals(phrase.getValue(), "Chuck Norris");
	}

	@Test
	public void testPersonMultiple() {
		Sentence sentence = new Sentence("Today I met Donald Trump hanging out with Barack Obama and Putin. They were laughing alot.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.PERSON);

		assertEquals(phrase1.getType(), EBaseType.PERSON);
		assertEquals(phrase1.getValue(), "Donald Trump");
		
		
		Phrase phrase2 = sentence.popPhraseOfType(EBaseType.PERSON);

		assertEquals(phrase2.getType(), EBaseType.PERSON);
		assertEquals(phrase2.getValue(), "Barack Obama");
		
		Phrase phrase3 = sentence.popPhraseOfType(EBaseType.PERSON);

		assertEquals(phrase3.getType(), EBaseType.PERSON);
		assertEquals(phrase3.getValue(), "Putin");
	}
	
	@Test
	public void testDuration() {
		Sentence sentence = new Sentence("I was waiting for at least two weeks for my new iPhone.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.DURATION);

		assertEquals(phrase1.getType(), EBaseType.DURATION);
		assertEquals(phrase1.getValue(), "at least two weeks");
		
	}
	
	@Test
	public void testMoney() {
		Sentence sentence = new Sentence("My car was on sale and I got a bounty of 100 dollar.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.MONEY);

		assertEquals(phrase1.getType(), EBaseType.MONEY);
		assertEquals(phrase1.getValue(), "100 dollar");
		
	}
	
	@Test
	public void testLocation() {
		Sentence sentence = new Sentence("Yesterday I wanted to fly to Dubai.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.LOCATION);

		assertEquals(phrase1.getType(), EBaseType.LOCATION);
		assertEquals(phrase1.getValue(), "Dubai");
		
	}
	
	@Test
	public void testNumber() {
		Sentence sentence = new Sentence("Thank you thousand times!", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.NUMBER);

		assertEquals(phrase1.getType(), EBaseType.NUMBER);
		assertEquals(phrase1.getValue(), "thousand");
		
	}
	
	@Test
	public void testOrdinal() {
		Sentence sentence = new Sentence("I will be the first in this race!", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.ORDINAL);

		assertEquals(phrase1.getType(), EBaseType.ORDINAL);
		assertEquals(phrase1.getValue(), "first");
		
	}
	
	@Test
	public void testOrganization() {
		Sentence sentence = new Sentence("Facebook will take over the world if we don't stop them.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.ORGANIZATION);

		assertEquals(phrase1.getType(), EBaseType.ORGANIZATION);
		assertEquals(phrase1.getValue(), "Facebook");
		
	}
	
	@Test
	public void testPercent() {
		Sentence sentence = new Sentence("If the glass is half empty, it is only 50 percent full.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.PERCENT);

		assertEquals(phrase1.getType(), EBaseType.PERCENT);
		assertEquals(phrase1.getValue(), "50 percent");
		
	}
	
	@Test
	public void testTime() {
		Sentence sentence = new Sentence("It was 12 o'clock when the New Year 's Eve rockets hit the sky.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.TIME);

		assertEquals(phrase1.getType(), EBaseType.TIME);
		assertEquals(phrase1.getValue(), "12 o'clock");
		
		Phrase phrase2 = sentence.popPhraseOfType(EBaseType.DATE);
		
		assertEquals(phrase2.getType(), EBaseType.DATE);
		assertEquals(phrase2.getValue(), "New Year 's Eve");
		
	}

}
