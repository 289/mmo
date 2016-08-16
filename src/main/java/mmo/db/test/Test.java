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

//        Transaction.begin();
//        try{
//            role.setNickname("lisi");
//        } catch (Exception e){
//            Transaction.current().rollBack();
//            Transaction.destroy();
//        } finally {
//            if(Transaction.current() != null){
//                Transaction.current().commit();
//                Transaction.destroy();
//            }
//        }
        new Thread(){
            @Override
            public void run() {
                System.out.println(1);
                role.setNickname("zhangsan");
                System.out.println(2);
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                System.out.println(3);
                role.setNickname("lisi");
                System.out.println(4);
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                System.out.println(5);
                role.update();
                System.out.println(6);
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                System.out.println(7);
                role.update();
                System.out.println(8);
            }
        }.start();

//        System.out.println(roleMapper.selectByPrimaryKey(1).getNickname());

    }
}
