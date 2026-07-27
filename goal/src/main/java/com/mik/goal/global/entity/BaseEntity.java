package com.mik.goal.global.entity;

public interface BaseEntity {

    void createHistory(Role role);

    void updateHistory();

    void updateHistory(Role role);

    void softDelete();

    void softDelete(Role role);

    void softDeleteCancel();

    void softDeleteCancel(Role role);


}
