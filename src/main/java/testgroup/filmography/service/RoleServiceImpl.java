package testgroup.filmography.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testgroup.filmography.userDAO.RoleDao;

@Service
public class RoleServiceImpl implements RoleService {


    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao=roleDao;
    }

    @Override
    @Transactional
    public void deleteRole(long id) {
        roleDao.deleteRole(id);
    }
}
