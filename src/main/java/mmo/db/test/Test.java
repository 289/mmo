package mmo.db.test;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mmo.ClazzMgr;
import mmo.db.MapperMgr;
import mmo.db.Transaction;
import mmo.db.role.entity.Role;
import mmo.db.role.mapper.RoleMapper;

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
//        Role role = new Role(2,"test",3, new HashMap<>());
//        roleMapper.insert(role);
        Role role = roleMapper.selectByPrimaryKey(2);

        new Thread(){
            @Override
            public void run() {
                Transaction.begin();
                try {
                    role.getTestJson().put("first","t1");
                    role.getTestJson().put("second","t2");
//            throw new RuntimeException();
                    Transaction.commit();
                } catch (Exception e){
                    Transaction.rollBack();
                } finally {
                    Transaction.destroy();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                role.save();
            }
        }.start();

        System.out.println(new ObjectMapper().writeValueAsString(roleMapper.selectByPrimaryKey(2)));

    }
}
