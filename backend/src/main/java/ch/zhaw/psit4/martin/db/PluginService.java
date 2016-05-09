package ch.zhaw.psit4.martin.db;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* This class provides methods to easily save and read plugin from the
* database.
* 
* @version 0.0.1-SNAPSHOT
*/
@Service
public class PluginService {

   private PluginDao pluginDao;
   
   public void setPluginDao(PluginDao pluginDao) {
       this.pluginDao = pluginDao;
   }

   @Transactional
   public void addPlugin(Plugin plugin) {
       this.pluginDao.add(plugin);
   }

   @Transactional
   public List<Plugin> listPlugins() {
       return this.pluginDao.listPlugins();
   }
   
   @Transactional
   public List<Plugin> getPluginsByUUID(String uuid) {
       return this.pluginDao.getByUUID(uuid);
   }

   @Transactional
   public Plugin getPluginById(int id) {
       return pluginDao.getById(id);
   }

   /**
    * USE WITH CAUTION!!
    * @param plugin
    */
   @Transactional
   public void updatePlugin(Plugin plugin) {
       this.pluginDao.updatePlugin(plugin);
   }

   @Transactional
   public void removePlugin(int id) {
       this.pluginDao.removePlugin(id);
   }



}
