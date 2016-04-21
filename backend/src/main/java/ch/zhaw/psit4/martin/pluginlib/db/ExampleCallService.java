/**
 * 
 */
package ch.zhaw.psit4.martin.pluginlib.db;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class provides methods to easily save and read ExampleCalls from the
 * database.
 * 
 * @author Daniel Zuerrer
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ExampleCallService {
    
    private ExampleCallDao exampleCallDao;
    
    public void setExampleCallDao(ExampleCallDao exampleCallDao) {
        this.exampleCallDao = exampleCallDao;
    }

    @Transactional
    public void addExampleCall(ExampleCall exampleCall) {
        this.exampleCallDao.add(exampleCall);
    }

    @Transactional
    public List<ExampleCall> listExampleCalls() {
        return this.exampleCallDao.listExampleCalls();
    }
    
    @Transactional
    public List<ExampleCall> getRandomExcampleCalls(){
        return this.exampleCallDao.getRandomExampleCalls();
    }


}
