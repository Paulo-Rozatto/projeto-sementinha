package db.dao;

import java.util.List;

/**
 *
 * @author paulo
 * @param <Object>
 */
public interface IDAO<Object> {
    public boolean create(Object o);
    public List<Object> read();
    public Object read(int id);
    public boolean update(Object o);
    public boolean delete(int i);
}
