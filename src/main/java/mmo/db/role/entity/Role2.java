//package mmo.db.role.entity;
//
//import mmo.db.DbEntity;
//import mmo.db.MapperMgr;
//import mmo.db.role.mapper.RoleMapper;
//import mmo.db.trans.TransLog;
//import mmo.db.trans.Transaction;
//
//public class Role2 extends DbEntity {
//    private Integer id;
//
//    private String nickname;
//
//    private Integer level;
//
//    public Role2(Integer id, String nickname, Integer level) {
//        this.id = id;
//        this.nickname = nickname;
//        this.level = level;
//    }
//
//    public Role2() {
//
//
//        super();
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getNickname() {
//        return nickname;
//    }
//
//    public void setNickname(String nickname) {
//        this.nickname = nickname == null ? null : nickname.trim();
//    }
//
//    public Integer getLevel() {
//        return level;
//    }
//
//    public void setLevel(final Integer level) {
//        if(this.level != null && this.level.equals(level)){
//            return;
//        }
//        Transaction transaction = Transaction.current();
//        if(transaction == null || isMirror){
//            this.level = level;
//            return;
//        }
//        final Integer curr = this.level;
//        transaction.addTRecord(new TransLog() {
//            @Override
//            public void commit() {
//                Role2 role = getMirror();
//                role.setLevel(level);
//                role.onUpdate();
//            }
//
//            @Override
//            public void rollback() {
//                Role2.this.level = curr;
//            }
//        });
//    }
//
//    @Override
//    public void initMirror() {
//        Role2 role = new Role2();
//        role.isMirror = true;
//        mirror = role;
//    }
//
//    private RoleMapper getMapper(){
//        return MapperMgr.getMapper(RoleMapper.class);
//    }
//
//    public void update(){
//        Role2 mirror = getMirror();
//        getMapper().updateByPrimaryKeySelective(mirror);
//        mirror.nickname = null;
//        mirror.level = null;
//    }
//
//}