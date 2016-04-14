package ch.zhaw.psit4.martin.pluginlib.db.author;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.zhaw.psit4.martin.pluginlib.db.keyword.Keyword;
import ch.zhaw.psit4.martin.pluginlib.db.keyword.KeywordDao;

/**
* This class provides methods to easily save and read Author from the
* database.
* 
* @version 0.0.1-SNAPSHOT
*/
@Service
public class AuthorService {

   private AuthorDao authorDao;
   
   public void setAuthorDao(AuthorDao authorDao) {
       this.authorDao = authorDao;
   }

   @Transactional
   public void addAuthor(Author author) {
       this.authorDao.add(author);
   }

   @Transactional
   public List<Author> listAuthors() {
       return this.authorDao.listAuthors();
   }

   @Transactional
   public Author getAuthorById(int id) {
       return authorDao.getById(id);
   }

   /**
    * USE WITH CAUTION!!
    * @param author
    */
   @Transactional
   public void updateAuthor(Author author) {
       this.authorDao.updateAuthor(author);
   }

   @Transactional
   public void removeAuthor(int id) {
       this.authorDao.removeAuthor(id);
   }



}
