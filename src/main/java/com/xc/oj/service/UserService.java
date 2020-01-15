package com.xc.oj.service;

import com.xc.oj.entity.Problem;
import com.xc.oj.entity.User;
import com.xc.oj.repository.ProblemRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.repository.UserRepository;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProblemRepository problemRepository;

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
    private Pattern passwordPattern=Pattern.compile("^[\\w~`!@#$%^&*()_+-=/?\\|<>,.;\\[\\]{}'\":]{6,16}$");
    private Pattern usernamePattern=Pattern.compile("^[\\w]{3,16}$");
    private Pattern emailPattern=Pattern.compile("^[\\w-]+@[\\w.-]+$");

    public responseBase<List<User>> listAll(){
        return responseBuilder.success(userRepository.findAll());
    }
    public responseBase<String> register(User user) {
        if (!usernamePattern.matcher(user.getUsername()).matches())
            return responseBuilder.fail(responseCode.USERNAME_INVALID);
        if (userRepository.existsByUsername(user.getUsername()))
            return responseBuilder.fail(responseCode.USERNAME_EXISTS);
        if (!passwordPattern.matcher(user.getPassword()).matches())
            return responseBuilder.fail(responseCode.PASSWORD_INVALID);
        if (!emailPattern.matcher(user.getEmail()).matches())
            return responseBuilder.fail(responseCode.EMAIL_INVALID);
        if (userRepository.existsByEmail(user.getEmail()))
            return responseBuilder.fail(responseCode.EMAIL_EXISTS);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreateTime(new Timestamp(new Date().getTime()));
        user.setEmail(user.getEmail());
        user.setNickname(user.getUsername());
        user.setRealname(user.getUsername());
        user.setAcceptedId(new ArrayList<>());
        user.setAcceptedNumber(0);
        user.setSubmissionNumber(0);
        user.setType("common user");
        user.setDisabled(false);
        userRepository.save(user);
        return responseBuilder.success();
    }
    public responseBase<String> login(String username, String password) {
        List<User> users=userRepository.findByUsername(username);
        if(users.isEmpty()||!encoder.matches(password,users.get(0).getPassword()))
            return responseBuilder.fail(responseCode.LOGIN_FAIL);
        if(users.get(0).getDisabled())
            return responseBuilder.fail(responseCode.USER_BLOCKED);
        users.get(0).setLastLoginTime(new Timestamp(new Date().getTime()));
        userRepository.save(users.get(0));
        return responseBuilder.success();
    }
    public responseBase<String> update(long id, User user){
        User data=userRepository.findById(id).orElse(null);
        if(data==null)
            return responseBuilder.fail(responseCode.USER_NOT_EXIST);
        if(user.getEmail()!=null)
            data.setEmail(user.getEmail());
        if(user.getAvatar()!=null)
            data.setAvatar(user.getAvatar());
        if(user.getNickname()!=null)
            data.setNickname(user.getNickname());
        if(user.getRealname()!=null)
            data.setRealname(user.getRealname());
//        if(user.getType()!=null)                  这些似乎是管理员接口的权限，只允许修改个人信息
//            data.setType(user.getType());
//        if(user.getDisabled()!=null)
//            data.setDisabled(user.getDisabled());
        userRepository.save(data);
        return responseBuilder.success();
    }
    public responseBase<String> add_submission_result(long uid,long pid,boolean isAc){//是题库的pid，比赛走add_contest_submission_accepted
        User user=userRepository.findById(uid).orElse(null);
        if(user==null)
            return responseBuilder.fail(responseCode.USER_NOT_EXIST);
        Problem prob=problemRepository.findById(pid).orElse(null);
        if(prob==null)
            return responseBuilder.fail(responseCode.PROBLEM_NOT_EXIST);
        if(isAc) {
            List<Integer> accId = user.getAcceptedId();
            if (!accId.contains(pid)) {
                accId.add((int) pid);
                user.setAcceptedId(accId);
                user.setAcceptedNumber(user.getAcceptedNumber() + 1);
                prob.setAcceptedNumber(prob.getAcceptedNumber() + 1);
            }
        }
        user.setSubmissionNumber(user.getSubmissionNumber()+1);
        prob.setSubmissionNumber(prob.getSubmissionNumber()+1);
        userRepository.save(user);
        problemRepository.save(prob);
        return responseBuilder.success();
    }

    public responseBase<String> delete(long id){
        User user=userRepository.findById(id).orElse(null);
        if(user==null)
            return responseBuilder.fail(responseCode.USER_NOT_EXIST);
        userRepository.deleteById(id);
        return responseBuilder.success();
    }
}