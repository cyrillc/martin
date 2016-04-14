package ch.zhaw.psit4.martin.frontend;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.aiController.AIControllerFacade;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class FrontendControllerTest {

    @Mock
    private AIControllerFacade aiController;
    
    @InjectMocks
    FrontendController controller;
    
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void ElaborateRequestReturnsResponseObjectFromObject(){
        Response response = new Response("Hallo");
        Response response1;
        Request request = new Request("ciao");
        when(aiController.elaborateRequest(request)).thenReturn(response);    
        
        response1 = aiController.elaborateRequest(request);
        assertTrue(response1.equals(response));       
    }
    
    
    
    
    
}
