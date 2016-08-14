package mmo.db.test;


import mmo.ClazzMgr;
import mmo.db.MapperMgr;
import mmo.db.role.entity.Role;
import mmo.db.role.mapper.RoleMapper;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Jin Shuai
 */
public class Test {
    public static void main(String[] args) {
        test1();
//        test2();
    }

    private static void test2() {
        ReentrantReadWriteLock reLock = new ReentrantReadWriteLock();

        reLock.readLock().tryLock();
        reLock.readLock().lock();
        try {
            System.out.println(123);
        } finally {
            reLock.readLock().unlock();
        }




    }

    public static void test1(){
        ClazzMgr.init();
        MapperMgr.init(ClazzMgr.getMapperClasses());

        RoleMapper roleMapper = MapperMgr.getMapper(RoleMapper.class);
        Role role = roleMapper.selectByPrimaryKey(1);

        Role role1 = new Role();
        role1.setId(1);
//        role1.setLevel(19);
        role1.setNickname("haha");
        roleMapper.updateByPrimaryKeySelective(role1);
//

        System.out.println(roleMapper.selectByPrimaryKey(1).getNickname());
    }
}
