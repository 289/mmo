package mmo.db;

import mmo.db.role.mapper.IRoleMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jin Shuai
 */
public class MapperMgr {
    private static Map<DBEnvironment, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<>();
    private static ConcurrentHashMap<Class<? extends MybatisMapper>, MybatisMapper> mapperMap = new ConcurrentHashMap<>();

    public static boolean init(Set<Class<? extends MybatisMapper>> mapperClasses) {
        initSessionFactory();
        initMapper(mapperClasses);
        return true;
    }

//    public static <T extends IGamedataMapper> T getGameDataMapper(Class<T> mapper) {
//        return (T) getMapper(mapper);
//    }

    public static <T extends IRoleMapper> T getRoleMapper(Class<T> mapper) {
        return (T) getMapper(mapper);
    }

//    public static <T extends ILoginMapper> T getLoginMapper(Class<T> mapper) {
//        return (T) getMapper(mapper);
//    }

//    public static <T extends IAccountMapper> T getAccountMapper(Class<T> mapper) {
//        return (T) getMapper(mapper);
//    }

    public static <T extends MybatisMapper> T getMapper(Class<T> clazz) {
        if (mapperMap.containsKey(clazz)) {
            return (T) mapperMap.get(clazz);
        }
        return null;
    }

    protected static List<MybatisMapper> getMapperByDb(DBEnvironment dbEnvironment) {
        List<MybatisMapper> ret = new ArrayList<>();
        for (Map.Entry<Class<? extends MybatisMapper>, MybatisMapper> entry : mapperMap.entrySet()) {
            if (dbEnvironment.mapperClazz.isAssignableFrom(entry.getKey())) {
                ret.add(entry.getValue());
            }
        }
        return ret;
    }

    private static void initSessionFactory() {
        for (DBEnvironment dbEnvironment : DBEnvironment.values()) {
            initSessionFactory0(dbEnvironment);
        }
    }

    private static void initSessionFactory0(DBEnvironment db) {
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(db.dbConfigFile);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, db.name());
            sqlSessionFactoryMap.put(db, sqlSessionFactory);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static void initMapper(Set<Class<? extends MybatisMapper>> mapperClasses) {
        for (Class<? extends MybatisMapper> clazz : mapperClasses) {
            if(!clazz.isInterface() || !MybatisMapper.class.isAssignableFrom(clazz) || clazz == MybatisMapper.class){
                continue;
            }

            MybatisMapper mapper = createMapper(clazz);

            if(mapper != null){
                mapperMap.putIfAbsent(clazz, mapper);
            }
        }
    }

    protected static <T extends MybatisMapper> T createMapper(Class<T> clazz) {
        if(clazz == null || !clazz.isInterface()){
            return null;
        }
        DBEnvironment db = DBEnvironment.getDBByMapperClazz(clazz);

        if (db == null) {
            return null;
        }

        if (!sqlSessionFactoryMap.containsKey(db)) {
            throw new IllegalArgumentException("DB not exist! DB:" + db.name());
        }
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryMap.get(db);
        return MapperProxy.bind(clazz, sqlSessionFactory);
    }

    private static class MapperProxy implements InvocationHandler {
        private Class mapperClazz;
        private SqlSessionFactory sqlSessionFactory;

        private MapperProxy(Class mapperClazz, SqlSessionFactory sqlSessionFactory) {
            this.mapperClazz = mapperClazz;
            this.sqlSessionFactory = sqlSessionFactory;
        }

        @SuppressWarnings("unchecked")
        private static <T extends MybatisMapper> T bind(Class<T> mapperClazz, SqlSessionFactory sqlSessionFactory) {
            return (T) Proxy.newProxyInstance(sqlSessionFactory.getClass().getClassLoader(),
                    new Class[]{mapperClazz}, new MapperProxy(mapperClazz, sqlSessionFactory));
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object object = null;
            SqlSession sqlSession = null;
            try {
                sqlSession = sqlSessionFactory.openSession();
                Object mapperProxy = sqlSession.getMapper(mapperClazz);
                object = method.invoke(mapperProxy, args);
                sqlSession.commit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sqlSession.close();
            }
            return object;
        }
    }

}
