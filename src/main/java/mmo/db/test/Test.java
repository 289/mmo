package mmo.db.test;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mmo.ClazzMgr;
import mmo.db.MapperMgr;
import mmo.db.Transaction;
import mmo.db.role.entity.Role;
import mmo.db.role.mapper.RoleMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Shuai
 */
public class Test {
    public static void main(String[] args) throws JsonProcessingException {
        test1();
    }

    public static void test1() throws JsonProcessingException {
        ClazzMgr.init();
        MapperMgr.init(ClazzMgr.getMapperClasses());

        RoleMapper roleMapper = MapperMgr.getMapper(RoleMapper.class);
        Role role = roleMapper.selectByPrimaryKey(3);
//        Role role = new Role();
//        role.setId(4);
//        role.setLevel(2);
//        role.setNickname("play");

        List<String> val = new ArrayList<>();
        val.add("haha");
        val.add("hehe");
        Transaction.begin();
        try {
            role.getTestList().add("test");
            role.getTestList().remove("test");
            role.getTestList().addAll(val);
            role.getTestList().remove("test");
            role.getTestList().removeAll(val);
            role.getTestList().set(2, "test");
            role.getTestList().clear();
            throw new RuntimeException();
//            Transaction.commit();
        } catch (Exception e){
            Transaction.rollBack();
        }finally {
            Transaction.destroy();
        }

//        role.getTestList().addAll(val);
//        role.getTestJson().put("test1", "test2");

//        roleMapper.insert(role);
//
//        System.out.println(new ObjectMapper().writeValueAsString(roleMapper.selectByPrimaryKey(4)));
        System.out.println(new ObjectMapper().writeValueAsString(role.getTestList()));

    }
}
