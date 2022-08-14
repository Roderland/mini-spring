package org.example.spring.test.dao;

import java.util.HashMap;
import java.util.Map;

public class UserDao {
    private static final Map<String, String> hashMap = new HashMap<>();

    public void initDataMethod() {
        hashMap.put("10001", "spring-01");
        hashMap.put("10002", "spring-02");
    }

    public void destroyDataMethod() {
        System.out.println("invoke destroyDataMethod.");
    }

    public void putUserInfo(String uid, String info) {
        hashMap.put(uid, info);
    }

    public String queryUserName(String uid) {
        return hashMap.get(uid);
    }
}
