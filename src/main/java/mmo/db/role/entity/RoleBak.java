package mmo.db.role.entity;

import mmo.db.DbEntity;
import mmo.db.trans.TransLog;
import mmo.db.trans.Transaction;

public class RoleBak extends DbEntity {
    private Integer id;

    private String nickname;

    private Integer level;

    public RoleBak(Integer id, String nickname, Integer level) {
        this.id = id;
        this.nickname = nickname;
        this.level = level;
    }

    public RoleBak() {
        super();
    }

    public Integer getId() {
        return (Integer) commonGet(this, 1);
    }

    public void setId(final Integer id) {
        if (this.id != null && this.id.equals(id)) {
            return;
        }
        if (!isMirror) {
            transactionHandle(1, this.nickname, nickname);
        }
        commonSet(this, 1, id);
    }

    public String getNickname() {
        return (String) commonGet(this, 2);
    }

    public void setNickname(final String nickname) {
        if (this.nickname != null && this.nickname.equals(nickname)) {
            return;
        }
        if (!isMirror) {
            transactionHandle(2, this.nickname, nickname);
        }
        commonSet(this, 2, nickname);
    }

    public Integer getLevel() {
        return (Integer) commonGet(this, 3);
    }

    public void setLevel(final Integer level) {
        if (this.level != null && this.level.equals(level)) {
            return;
        }
        if (!isMirror) {
            transactionHandle(3, this.level, level);
        }
        commonSet(this, 3, level);
    }

    private void transactionHandle(final int index, Object old, final Object newVal){
        Transaction transaction = Transaction.current();
        if (transaction != null) {
            final Object curr = old;
            transaction.addTRecord(new TransLog() {
                @Override
                public void commit() {
                    updateToMirror(index, newVal);
                }

                @Override
                public void rollback() {
                    commonSet(RoleBak.this, index, curr);
                }
            });
        } else {
            updateToMirror(index, newVal);
        }
    }

    private void updateToMirror(int index, Object val){
        while (!state.compareAndSet(NONE_THREAD, WRITE_THREAD)){
            //spin
        }
        RoleBak mirror = getMirror();
        commonSet(mirror, index, val);
        mirror.onUpdate();
        state.set(NONE_THREAD);
    }

    private void commonSet(RoleBak role, int index, Object val){
        switch (index){
            case 1 : role.id = (Integer) val;break;
            case 2 : role.nickname = val == null ? null : ((String) val).trim();break;
            case 3 : role.level = (Integer) val;break;
            default: throw new RuntimeException();
        }
    }

    private Object commonGet(RoleBak role, int index){
        switch (index){
            case 1 : return role.id;
            case 2 : return role.nickname;
            case 3 : return role.level;
        }
        throw new RuntimeException();
    }

    @Override
    public void update() {
        RoleBak mirror = getMirrorThenClear();
        if(mirror == null){
            return;
        }
        switch (mirror.mark){
            case UPDATE : {
//                MapperMgr.getRoleMapper(RoleMapper.class).updateByPrimaryKeySelective(mirror);
                break;
            }
        }
    }

    @Override
    protected void initMirror() {
        RoleBak role = new RoleBak();
        role.initMirror0();
        role.id = this.id;
        mirrorEntity = role;
    }
}