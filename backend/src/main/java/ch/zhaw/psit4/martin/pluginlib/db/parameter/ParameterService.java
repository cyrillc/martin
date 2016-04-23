package ch.zhaw.psit4.martin.pluginlib.db.parameter;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* This class provides methods to easily save and read Parameter from the
* database.
* 
* @version 0.0.1-SNAPSHOT
*/
@Service
public class ParameterService {

   private ParameterDao parameterDao;
   
   public void setParameterDao(ParameterDao parameterDao) {
       this.parameterDao = parameterDao;
   }

   @Transactional
   public void addParameter(Parameter parameter) {
       this.parameterDao.add(parameter);
   }

   @Transactional
   public List<Parameter> listParameters() {
       return this.parameterDao.listParameters();
   }

   @Transactional
   public Parameter getParameterById(int id) {
       return parameterDao.getById(id);
   }

   /**
    * USE WITH CAUTION!!
    * @param parameter
    */
   @Transactional
   public void updateParameter(Parameter parameter) {
       this.parameterDao.updateParameter(parameter);
   }

   @Transactional
   public void removeParameter(int id) {
       this.parameterDao.removeParameter(id);
   }



}
