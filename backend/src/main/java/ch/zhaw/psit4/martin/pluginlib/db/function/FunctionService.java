package ch.zhaw.psit4.martin.pluginlib.db.function;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* This class provides methods to easily save and read Function from the
* database.
* 
* @version 0.0.1-SNAPSHOT
*/
@Service
public class FunctionService {

   private FunctionDao functionDao;
   
   public void setFunctionDao(FunctionDao functionDao) {
       this.functionDao = functionDao;
   }

   @Transactional
   public void addFunction(Function function) {
       this.functionDao.add(function);
   }

   @Transactional
   public List<Function> listFunctions() {
       return this.functionDao.listFunctions();
   }

   @Transactional
   public Function getFunctionById(int id) {
       return functionDao.getById(id);
   }
   
   @Transactional
   public List<Object[]> getByKeyword(String keyword){
	   return functionDao.getByKeyword(keyword);
   }

   /**
    * USE WITH CAUTION!!
    * @param function
    */
   @Transactional
   public void updateFunction(Function function) {
       this.functionDao.updateFunction(function);
   }

   @Transactional
   public void removeFunction(int id) {
       this.functionDao.removeFunction(id);
   }



}
