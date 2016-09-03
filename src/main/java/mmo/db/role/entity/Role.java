package mmo.db.role.entity;

import mmo.db.DbEntity;
import mmo.db.MapperMgr;
import mmo.db.role.mapper.RoleMapper;
import mmo.db.trans.TransLog;
import mmo.db.trans.Transaction;

public class Role extends DbEntity {
    private Integer id;

    private String nickname;

    private Integer level;

    public Role(Integer id, String nickname, Integer level) {
        this.id = id;
        this.nickname = nickname;
        this.level = level;
    }

    public Role() {
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
        this.id = id;
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
        this.nickname = nickname == null ? null : nickname.trim();
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
        this.level = level;
    }

    public void transactionHandle(final int index, Object old, final Object newVal){
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
                    commonSet(Role.this, index, curr);
                }
            });
        } else {
            updateToMirror(index, newVal);
        }
    }

    public void updateToMirror(int index, Object val){
        while (state.get() != WRITE_THREAD){
            //spin
            state.compareAndSet(NONE_THREAD, UPDATE_THREAD);
        }
        Role mirror = getMirror();
        commonSet(mirror, index, val);
        mirror.onUpdate();
        state.set(NONE_THREAD);
    }

    public Role getMirrorThenClear(){
        Role mirror = null;
        if(mirrorEntity != null && state.compareAndSet(NONE_THREAD, UPDATE_THREAD)){
            mirror = mirrorEntity.cast();
            mirrorEntity = null;
            state.set(NONE_THREAD);
        }
        return mirror;
    }

    public void commonSet(Role role, int index, Object val){
        switch (index){
            case 1 : break;
            case 2 : role.setLevel((Integer) val);break;
            case 3 : role.setNickname((String) val);break;
        }
    }

    public Object commonGet(Role role, int index){
        switch (index){
            case 1 : return role.id;
            case 2 : return role.nickname;
            case 3 : return role.level;
        }
        throw new RuntimeException();
    }

    @Override
    public void update() {
        Role mirror = getMirrorThenClear();
        if(mirror != null && mirror.mark == UPDATE){
            MapperMgr.getRoleMapper(RoleMapper.class).updateByPrimaryKeySelective(mirror);
        }
    }

    @Override
    public void initMirror() {
        Role role = new Role();
        role.initMirror0();
        role.id = this.id;
        mirrorEntity = role;
    }
}