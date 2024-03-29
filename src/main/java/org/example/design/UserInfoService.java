package org.example.design;

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public interface UserInfoService {
    List<String> getUserIdList();
}

class UserInfoServiceImpl implements UserInfoService {

    @Override
    public List<String> getUserIdList() {
        return Lists.newArrayList("1", "2", "3");
    }
}

class CachedUserInfoServiceImpl implements UserInfoService {
    private List<String> userIdList;

    private UserInfoServiceImpl userInfoServiceImpl = new UserInfoServiceImpl();

    CachedUserInfoServiceImpl() {
    }

    @Override
    public List<String> getUserIdList() {
        if (!CollectionUtils.isEmpty(this.userIdList)) {
            return this.userIdList;
        }
        List<String> result = userInfoServiceImpl.getUserIdList();
        this.userIdList = new ArrayList<>(result);
        return result;
    }
}

