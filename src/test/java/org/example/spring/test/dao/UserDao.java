package org.example.spring.test.dao;

import java.util.HashMap;
import java.util.Map;

public class UserDao {
    private static final Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("10001", "spring");
    }

    public void putUserInfo(String uid, String info) {
        hashMap.put(uid, info);
    }

    public String queryUserName(String uid) {
        return hashMap.get(uid);
    }
}
