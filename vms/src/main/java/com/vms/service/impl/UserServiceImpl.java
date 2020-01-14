package com.vms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vms.bean.Role;
import com.vms.bean.User;
import com.vms.config.PropertyConfig;
import com.vms.dao.RoleMapper;
import com.vms.dao.UserMapper;
import com.vms.service.UserService;
import com.vms.utils.CommonUtils;
import com.vms.utils.FastDFSClient;
import com.vms.utils.FileUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    PropertyConfig propertyConfig;

    @Autowired
    FastDFSClient fastDFSClient;

    @Override
    public List<User> findAllUser() {
        return userMapper.findAllUser();
    }

    @Override
    public PageInfo<User> findUserByWhere(Map<String, Object> where) {
        PageHelper.startPage((Integer) where.get("index"), (Integer)where.get("size"));
        PageInfo<User> info = new PageInfo<>(userMapper.findAllUserByWhere(where));
        return info;
    }

    @Override
    public User findUserDetail(String userName) {
        User user = userMapper.findByUserName(userName);
        if(user != null){
            List<Role> roles = roleMapper.findPermissionByUserId(user.getId());
            user.setRoleList(roles);
        }
        return user;
    }

    @Override
    public User findUserSimple(String userName) {
        return userMapper.findSimpleByUserName(userName);
    }

    @Override
    public List<User> findAllAndRole() {
        return userMapper.findAllAndRole();
    }

    @Override
    public void addUserAndRole(User user, HttpServletRequest request) throws Exception {

        //获取前端传过来的base64字符串，然后转换为文件对象再上传
        String base64Data = user.getAvatar();
        //获取图片后缀
        String suffix = base64Data.split(";base64,")[0].split("/")[1];

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String tempFileName = uuid+"."+suffix;
//        String imgFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static" +
//                "\\images\\avatars\\"+tempFileName;
        String imgFilePath = "/home/lmc/vms/temp/"+tempFileName;
        System.out.println(imgFilePath);
        FileUtils.base64ToFile(imgFilePath, base64Data);

        //上传文件到fastDFS
        String url = fastDFSClient.uploadBase64(FileUtils.fileToMultipart(imgFilePath));

        //获取缩略图的url
        String thump = "_80x80.";
        String[] arr = tempFileName.split("\\.");
        String thumpImgUrl = arr[0]+thump+arr[1];

        user.setAvatar(propertyConfig.getImgUrl()+url);
        user.setThumbAvatar(propertyConfig.getImgUrl()+thumpImgUrl);
        user.setPassword(new SimpleHash("md5", user.getPassword(), null, 2).toString());
        userMapper.insertUser(user);
        List<Role> roleList = user.getRoleList();
        for (Role r : roleList) {
            roleMapper.insertUserRole(user.getId(), r.getId());
        }
    }

    @Override
    public int deleteUser(Integer id) {
        return userMapper.delUserById(id);
    }
}
