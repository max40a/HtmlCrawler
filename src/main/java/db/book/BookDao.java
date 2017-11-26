package db.book;

import db.BadRequestException;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

public class BookDao {

    private QueryRunner queryRunner;

    public BookDao(DataSource dataSource) {
        this.queryRunner = new QueryRunner(dataSource);
    }

    public void saveBook(String urlToBook, String jsonBook) {
        String query = "INSERT INTO crawler.books (url, book) VALUES (?,?)";
        try {
            queryRunner.update(query, urlToBook, jsonBook);
        } catch (SQLException e) {
            String message = String.format("Exception occur in %s, when request occurred : %s", getClass().getName(), query);
            throw new BadRequestException(message, e);
        }
    }

    public boolean isBookExist(String urlToBook) {
        String query = "SELECT exists(SELECT url FROM crawler.books WHERE url=?)";
        try {
            return (queryRunner.query(query, rs -> {
                rs.next();
                return rs.getInt(1);
            }, urlToBook)) != 0;
        } catch (SQLException e) {
            String message = String.format("Exception occur in %s, when request occurred : %s", getClass().getName(), query);
            throw new BadRequestException(message, e);
        }
    }

    public void updateExistBook(String urlToBook, String jsonBook) {
        String query = "UPDATE crawler.books SET book=? WHERE url=?";
        try {
            queryRunner.update(query, jsonBook, urlToBook);
        } catch (SQLException e) {
            String message = String.format("Exception occur in %s, when request occurred : %s", getClass().getName(), query);
            throw new BadRequestException(message, e);
        }
    }
}