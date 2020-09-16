package testgroup.filmography.userDAO;

import testgroup.filmography.model.Role;

public interface RoleDao {

    Role getRoleById(long id);

    void deleteRole(long id);

}