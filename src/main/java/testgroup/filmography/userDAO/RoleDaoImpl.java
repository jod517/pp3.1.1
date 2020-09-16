package testgroup.filmography.userDAO;

import org.springframework.stereotype.Repository;
import testgroup.filmography.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role getRoleById(long id) {
        return entityManager.createQuery("select r from Role r where id = :id", Role.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public void deleteRole(long id) {
        entityManager.remove(getRoleById(id));
    }
}